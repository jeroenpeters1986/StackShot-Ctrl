package mtb.drivers.edsdk;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edsdk.Edsdk.EdsCompressQuality;
import edsdk.Edsdk.EdsImageSize;
import edsdk.Edsdk.EdsImageType;

public class CanonUtils {
	static class CameraArrayList extends ArrayList<CameraImageQuality> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		// sort an ArrayList on index
		public void sort() {
			Collections.sort(cameraList, new Comparator<CameraImageQuality>() {
				public int compare(CameraImageQuality a, CameraImageQuality b) {
					return Integer.signum(a.productName
							.compareTo(b.productName));
				}
			});
		}
	}
	
	//static ArrayList<CameraImageQuality> cameraList = new ArrayList<CameraImageQuality>();
	static CameraArrayList cameraList = new CameraArrayList();

	public static interface ImageType {
		public static final int None = EdsImageType.kEdsImageType_Unknown;
		public static final int Jpeg = EdsImageType.kEdsImageType_Jpeg;
		public static final int CRW = EdsImageType.kEdsImageType_CRW;
		public static final int RAW = EdsImageType.kEdsImageType_RAW;
		public static final int CR2 = EdsImageType.kEdsImageType_CR2;
	};
	public static interface ImageSize {
		public static final int Large = EdsImageSize.kEdsImageSize_Large;
		public static final int Middle = EdsImageSize.kEdsImageSize_Middle;
		public static final int Small = EdsImageSize.kEdsImageSize_Small;
		public static final int Middle1 = EdsImageSize.kEdsImageSize_Middle1;
		public static final int Middle2 = EdsImageSize.kEdsImageSize_Middle2;
		public static final int Small1 = EdsImageSize.kEdsImageSize_Small1;
		public static final int Small2 = EdsImageSize.kEdsImageSize_Small2;
		public static final int Small3 = EdsImageSize.kEdsImageSize_Small3;
		public static final int Unknown = EdsImageSize.kEdsImageSize_Unknown;
	};
	public static interface CompressQuality {
		public static final int Normal = EdsCompressQuality.kEdsCompressQuality_Normal;
		public static final int Fine = EdsCompressQuality.kEdsCompressQuality_Fine;
		public static final int Lossless = EdsCompressQuality.kEdsCompressQuality_Lossless;
		public static final int SuperFine = EdsCompressQuality.kEdsCompressQuality_SuperFine;
		public static final int Unknown = EdsCompressQuality.kEdsCompressQuality_Unknown;
	};	
	
	
	static class ImageFormat {
		String name;
		int type;
		int size;
		int width;
		int height;
		
		ImageFormat(String name, int type, int size, int x, int y){
			this.name=name;
			this.type=type;
			this.size=size;
			this.width=x;
			this.height=y;
		}
	}
	
	
	
	
	
	static class CameraImageQuality {
		String productName;
		double sensorWidth;
		double sensorHeight;
		int[] compressQuality;
		ArrayList<ImageFormat> formatList=new ArrayList<ImageFormat>();
		ArrayList<String[]> relationList=new ArrayList<String[]>();

		CameraImageQuality(String productName, double sensorWidth, double sensorHeight, int [] compressQuality, ImageFormat ... imageFormats) {
			this.productName=productName;
			this.compressQuality=compressQuality;
			this.sensorWidth=sensorWidth;
			this.sensorHeight=sensorHeight;
			for (ImageFormat element : imageFormats) {
			    formatList.add(element);
			  }
		}
		
		public void addRelation(String [] strtab){
			relationList.add(strtab);
		}
		
		// get a String[] constructed with an index of an ArrayList
		public String[] listMainFormats() {
			String[] list = new String[formatList.size()];

			for (int i = 0; i < formatList.size(); i++) {
					list[i] = formatList.get(i).name;
			}
			return list;
		}
		

		public String[] listSecondFormats(String mainFormatName) {
			String[] list = null;
			
			for (int i = 0; i < relationList.size(); i++) {
				if(relationList.get(i)[0].equals(mainFormatName)) {
					if((relationList.get(i).length-1)!=0) {
						list = new String[relationList.get(i).length-1];
						for(int j=0;j<list.length;j++) {
							list[j]=relationList.get(i)[j+1];
						}
					}
					break;
				}
			}
			return list;
		}

		public String[] listCompressFormats() {
			Field[] fields = CompressQuality.class.getFields();
			String[] list = new String[compressQuality.length];
			
			for(int i=0;i<compressQuality.length;i++) {
				
				for( Field field : fields ){
					try {
						if(field.getInt(CompressQuality.class) == compressQuality[i]){
							list[i]= field.getName();
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			return list;
		}
		
		public int findMainIndex(String formatName) {
			String[] list = listMainFormats();
			if(list==null) return -1;
			
			for (int i = 0; i < list.length; i++) {
					if(list[i].equals(formatName)) return i;
			}
			return -1;
		}

		public int findSecondIndex(String mainFormatName, String secondFormatName) {
			String[] list = listSecondFormats(mainFormatName);
			if(list==null) return -1;
			
			for (int i = 0; i < list.length; i++) {
					if(list[i].equals(secondFormatName)) return i;
			}
			return -1;
		}

		public int findCompressIndex(String compressName) {
			String[] list = listCompressFormats();
			if(list==null) return -1;

			for (int i = 0; i < list.length; i++) {
					if(list[i].equals(compressName)) return i;
			}
			return -1;
		}

		public int findMainIndexByValues(int type, int size) {
			String[] list = listMainFormats();
			if(list==null) return -1;
			
			for (int i = 0; i < list.length; i++) {
				ImageFormat fmt = getFormat(list[i]);
				if(fmt!=null && fmt.type==type && fmt.size==size) return i;
			}
			return -1;
		}

		public int findSecondIndexByValues(String mainFormatName, int type, int size) {
			String[] list = listSecondFormats(mainFormatName);
			if(list==null) return -1;

			//System.out.println(list.toString());
			
			for (int i = 0; i < list.length; i++) {
				ImageFormat fmt = getFormat(list[i]);
				if(fmt!=null && fmt.type==type && fmt.size==size) return i;
			}
			return -1;
		}

		public int findCompressIndexByValues(int compress) {
			for (int i = 0; i < compressQuality.length; i++) {
					if(compressQuality[i]==compress) return i;
			}
			return -1;
		}
		
		public ImageFormat getFormat(String formatName) {
			for (int i = 0; i < formatList.size(); i++) {
				if(formatList.get(i).name.equals(formatName)) {
					return formatList.get(i);
				}
			}
			return null;
		}
		public ImageFormat getFormatByValues(int type, int size) {
			for (int i = 0; i < formatList.size(); i++) {
				if(formatList.get(i).type==type && formatList.get(i).size==size) {
					return formatList.get(i);
				}
			}
			return null;
		}
		public int getCompressQuality(String compressName) {
			Field[] fields = CompressQuality.class.getFields();
			for( Field field : fields ){
				try {
					if(field.getName().equals(compressName))
						return field.getInt(CompressQuality.class);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			return -1;
		}
		
	}

	
	
	
	private static boolean constructed=false;
	public static void canonUtilsConstruct() {
		if(constructed) return;
		constructed=true;
		System.out.println("construct");
		// PTP Camera
		CameraImageQuality ciq;
		ciq = new CameraImageQuality("Canon EOS-1D Mark III",
				28.1, 18.7, new int[]{CompressQuality.Fine},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 3888, 2595),
				new ImageFormat("Jpeg Medium 1", ImageType.Jpeg, ImageSize.Middle1, 3456, 2304),
				new ImageFormat("Jpeg Medium 2", ImageType.Jpeg, ImageSize.Middle2, 2816, 1880),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 1936, 1288),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 3888, 2595),
				new ImageFormat("sRAW", ImageType.CR2, ImageSize.Small, 1936, 1288)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"sRAW", "Jpeg Small"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium 1"});
		ciq.addRelation(new String [] {"Jpeg Medium 2"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);
		
		ciq = new CameraImageQuality("Canon EOS 40D",
				22.2, 14.8, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 3888, 2595),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 2816, 1880),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 1936, 1288),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 3888, 2595),
				new ImageFormat("sRAW", ImageType.CR2, ImageSize.Small, 1936, 1288)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium","Jpeg Small"});
		ciq.addRelation(new String [] {"sRAW", "Jpeg Small", "Jpeg Medium","Jpeg Small"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);

		ciq = new CameraImageQuality("Canon EOS-1Ds Mark III",
				36.0, 24.0, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5616, 3744),
				new ImageFormat("Jpeg Medium 1", ImageType.Jpeg, ImageSize.Middle1, 4992, 3328),
				new ImageFormat("Jpeg Medium 2", ImageType.Jpeg, ImageSize.Middle2, 4080, 2720),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2784, 1856),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5616, 3744),
				new ImageFormat("sRAW", ImageType.CR2, ImageSize.Small, 2784, 1856)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium 1", "Jpeg Medium 2 ","Jpeg Small"});
		ciq.addRelation(new String [] {"sRAW", "Jpeg Small", "Jpeg Medium 1", "Jpeg Medium 2 ","Jpeg Small"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium 1"});
		ciq.addRelation(new String [] {"Jpeg Medium 2"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);

		ciq = new CameraImageQuality("Canon EOS 450D",
				22.2, 14.8, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 4272, 2848),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3088, 2056),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2256, 1504),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 4272, 2848)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);

		ciq = new CameraImageQuality("Canon EOS 1000D",
				22.2, 14.8, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 3888, 2592),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 2816, 1880),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 1936, 1288),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 3888, 2592)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);

		ciq = new CameraImageQuality("Canon EOS 5D Mark II",
				36, 24, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5616, 3744),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 4080, 2720),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2784, 1856),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5616, 3744),
				new ImageFormat("sRAW1", ImageType.CR2, ImageSize.Middle, 4080, 2720),
				new ImageFormat("sRAW2", ImageType.CR2, ImageSize.Small, 2784, 1856)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"sRAW1", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"sRAW2", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);

		
		ciq = new CameraImageQuality("Canon EOS 50D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 4752, 3168),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3456, 2304),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2353, 1568),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 4752, 3168),
				new ImageFormat("sRAW1", ImageType.CR2, ImageSize.Middle, 3267, 2178),
				new ImageFormat("sRAW2", ImageType.CR2, ImageSize.Small, 2376, 1584)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"sRAW1", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"sRAW2", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);

		
		ciq = new CameraImageQuality("Canon EOS 500D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 4752, 3168),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3088, 2056),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2256, 1504),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 4752, 3168)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);

		ciq = new CameraImageQuality("Canon EOS 7D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5184, 3456),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3456, 2304),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2592, 1728),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5184, 3456),
				new ImageFormat("M-RAW", ImageType.CR2, ImageSize.Middle, 3888, 2592),
				new ImageFormat("S-RAW", ImageType.CR2, ImageSize.Small, 2592, 1728)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"M-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"S-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);
		
		ciq = new CameraImageQuality("Canon EOS-1D Mark IV",
				27.9, 18.6, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 4896, 3264),
				new ImageFormat("Jpeg Medium 1", ImageType.Jpeg, ImageSize.Middle1, 4320, 2880),
				new ImageFormat("Jpeg Medium 2", ImageType.Jpeg, ImageSize.Middle2, 3552, 2368),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2448, 1632),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 4896, 3264),
				new ImageFormat("M-RAW", ImageType.CR2, ImageSize.Middle, 3672, 2448),
				new ImageFormat("S-RAW", ImageType.CR2, ImageSize.Small, 2448, 1632)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium 1", "Jpeg Medium 2", "Jpeg Small"});
		ciq.addRelation(new String [] {"M-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Medium 2", "Jpeg Small"});
		ciq.addRelation(new String [] {"S-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Medium 2", "Jpeg Small"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium 1"});
		ciq.addRelation(new String [] {"Jpeg Medium 2"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);
		
		ciq = new CameraImageQuality("Canon EOS 550D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5184, 3456),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3456, 2304),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2592, 1728),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5184, 3456)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);
		
		ciq = new CameraImageQuality("Canon EOS 60D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5184, 3456),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3456, 2304),
				new ImageFormat("Jpeg Small 1", ImageType.Jpeg, ImageSize.Small1, 2592, 1728),
				new ImageFormat("Jpeg Small 2", ImageType.Jpeg, ImageSize.Small2, 1920, 1280),
				new ImageFormat("Jpeg Small 3", ImageType.Jpeg, ImageSize.Small3, 720, 480),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5184, 3456),
				new ImageFormat("M-RAW", ImageType.CR2, ImageSize.Large, 3888, 2592),
				new ImageFormat("S-RAW", ImageType.CR2, ImageSize.Large, 2592, 1728)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"M-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"S-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small1"});
		ciq.addRelation(new String [] {"Jpeg Small2"});
		ciq.addRelation(new String [] {"Jpeg Small3"});
		cameraList.add(ciq);
		
		ciq = new CameraImageQuality("Canon EOS 600D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5184, 3456),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3456, 2304),
				new ImageFormat("Jpeg Small 1", ImageType.Jpeg, ImageSize.Small1, 2592, 1728),
				new ImageFormat("Jpeg Small 2", ImageType.Jpeg, ImageSize.Small2, 1920, 1280),
				new ImageFormat("Jpeg Small 3", ImageType.Jpeg, ImageSize.Small3, 720, 480),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5184, 3456)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small1"});
		ciq.addRelation(new String [] {"Jpeg Small2"});
		ciq.addRelation(new String [] {"Jpeg Small3"});
		cameraList.add(ciq);
		
		ciq = new CameraImageQuality("Canon EOS 1100D",
				22.2, 14.7, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 4272, 2848),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3088, 2056),
				new ImageFormat("Jpeg Small 1", ImageType.Jpeg, ImageSize.Small1, 2256, 1504),
				new ImageFormat("Jpeg Small 2", ImageType.Jpeg, ImageSize.Small2, 1920, 1280),
				new ImageFormat("Jpeg Small 3", ImageType.Jpeg, ImageSize.Small3, 720, 480),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 4272, 2848)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small1"});
		ciq.addRelation(new String [] {"Jpeg Small2"});
		ciq.addRelation(new String [] {"Jpeg Small3"});
		cameraList.add(ciq);
		
		ciq = new CameraImageQuality("Canon EOS 5D Mark III",
				36.0, 24.0, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5760, 3840),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3840, 2560),
				new ImageFormat("Jpeg Small 1", ImageType.Jpeg, ImageSize.Small1, 2880, 1920),
				new ImageFormat("Jpeg Small 2", ImageType.Jpeg, ImageSize.Small2, 1920, 1280),
				new ImageFormat("Jpeg Small 3", ImageType.Jpeg, ImageSize.Small3, 720, 480),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5760, 3840),
				new ImageFormat("M-RAW", ImageType.CR2, ImageSize.Large, 3840, 2560),
				new ImageFormat("S-RAW", ImageType.CR2, ImageSize.Large, 2880, 1920)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"M-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"S-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small1"});
		ciq.addRelation(new String [] {"Jpeg Small2"});
		ciq.addRelation(new String [] {"Jpeg Small3"});
		cameraList.add(ciq);
		
		ciq = new CameraImageQuality("Canon EOS-1D X",
				36.0, 24.0, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5184, 3456),
				new ImageFormat("Jpeg Medium 1", ImageType.Jpeg, ImageSize.Middle1, 4608, 3072),
				new ImageFormat("Jpeg Medium 2", ImageType.Jpeg, ImageSize.Middle2, 3456, 2304),
				new ImageFormat("Jpeg Small", ImageType.Jpeg, ImageSize.Small, 2592, 1728),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5184, 3456),
				new ImageFormat("M-RAW", ImageType.CR2, ImageSize.Large, 3888, 2592),
				new ImageFormat("S-RAW", ImageType.CR2, ImageSize.Large, 2592, 1728)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium 1", "Jpeg Medium 2", "Jpeg Small"});
		ciq.addRelation(new String [] {"M-RAW", "Jpeg Large", "Jpeg Medium 1", "Jpeg Medium 2", "Jpeg Small"});
		ciq.addRelation(new String [] {"S-RAW", "Jpeg Large", "Jpeg Medium 1", "Jpeg Medium 2", "Jpeg Small"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium 1"});
		ciq.addRelation(new String [] {"Jpeg Medium 2"});
		ciq.addRelation(new String [] {"Jpeg Small"});
		cameraList.add(ciq);

		ciq = new CameraImageQuality("Canon EOS 650D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5184, 3456),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3456, 2304),
				new ImageFormat("Jpeg Small 1", ImageType.Jpeg, ImageSize.Small1, 2592, 1728),
				new ImageFormat("Jpeg Small 2", ImageType.Jpeg, ImageSize.Small2, 1920, 1280),
				new ImageFormat("Jpeg Small 3", ImageType.Jpeg, ImageSize.Small3, 720, 480),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5184, 3456)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small1"});
		ciq.addRelation(new String [] {"Jpeg Small2"});
		ciq.addRelation(new String [] {"Jpeg Small3"});
		cameraList.add(ciq);

		ciq = new CameraImageQuality("Canon EOS 6D",
				36.0, 24.0, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5472, 3648),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3468, 2432),
				new ImageFormat("Jpeg Small 1", ImageType.Jpeg, ImageSize.Small1, 2736, 1824),
				new ImageFormat("Jpeg Small 2", ImageType.Jpeg, ImageSize.Small2, 1920, 1280),
				new ImageFormat("Jpeg Small 3", ImageType.Jpeg, ImageSize.Small3, 720, 480),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5472, 3648),
				new ImageFormat("M-RAW", ImageType.CR2, ImageSize.Large, 4104, 2736),
				new ImageFormat("S-RAW", ImageType.CR2, ImageSize.Large, 2736, 1824)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"M-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"S-RAW", "Jpeg Large", "Jpeg Medium", "Jpeg Small 1", "Jpeg Small 2", "Jpeg Small 3"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small1"});
		ciq.addRelation(new String [] {"Jpeg Small2"});
		ciq.addRelation(new String [] {"Jpeg Small3"});
		cameraList.add(ciq);

		ciq = new CameraImageQuality("Canon EOS 700D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5184, 3456),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3456, 2304),
				new ImageFormat("Jpeg Small1", ImageType.Jpeg, ImageSize.Small1, 2592, 1728),
				new ImageFormat("Jpeg Small2", ImageType.Jpeg, ImageSize.Small2, 1920, 1280),
				new ImageFormat("Jpeg Small3", ImageType.Jpeg, ImageSize.Small3, 720, 480),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5184, 3456)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small1"});
		ciq.addRelation(new String [] {"Jpeg Small2"});
		ciq.addRelation(new String [] {"Jpeg Small3"});
		cameraList.add(ciq);
		
		
		ciq = new CameraImageQuality("Canon EOS 100D",
				22.3, 14.9, new int[]{CompressQuality.Fine, CompressQuality.Normal},
				new ImageFormat("Jpeg Large", ImageType.Jpeg, ImageSize.Large, 5184, 3456),
				new ImageFormat("Jpeg Medium", ImageType.Jpeg, ImageSize.Middle, 3456, 2304),
				new ImageFormat("Jpeg Small1", ImageType.Jpeg, ImageSize.Small1, 2592, 1728),
				new ImageFormat("Jpeg Small2", ImageType.Jpeg, ImageSize.Small2, 1920, 1280),
				new ImageFormat("Jpeg Small3", ImageType.Jpeg, ImageSize.Small3, 720, 480),
				new ImageFormat("RAW", ImageType.CR2, ImageSize.Large, 5184, 3456)
				);
		ciq.addRelation(new String [] {"RAW", "Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Large"});
		ciq.addRelation(new String [] {"Jpeg Medium"});
		ciq.addRelation(new String [] {"Jpeg Small1"});
		ciq.addRelation(new String [] {"Jpeg Small2"});
		ciq.addRelation(new String [] {"Jpeg Small3"});
		cameraList.add(ciq);
		cameraList.sort();
		
	}
	
	// get a String[] constructed with an index of an ArrayList
	public static String[] listCameras() {
		canonUtilsConstruct();
		String[] namelist = new String[cameraList.size()];

		for (int i = 0; i < cameraList.size(); i++) {
			namelist[i] = cameraList.get(i).productName;
		}
		return namelist;
	}
	
	public static int findIndexByProductName(String productName) {
		canonUtilsConstruct();
		int res = -1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				res = i;
				break;
			}
		}
		return res;
	}
	
	public static double getSensorWidth(String productName) {
		canonUtilsConstruct();
		double res = -1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				res = cameraList.get(i).sensorWidth;
				break;
			}
		}
		return res;
	}

	public static double getSensorHeight(String productName) {
		canonUtilsConstruct();
		double res = -1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				res = cameraList.get(i).sensorHeight;
				break;
			}
		}
		return res;
	}

	public static double getPixelX(String productName, int imageQuality) {
		canonUtilsConstruct();
		//find camera
		int camnum=-1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				camnum=i;
				break;
			}
		}
		//System.out.println("camnum:"+camnum);
		if(camnum==-1) return -1;
		
		//now convert value to type, size
		//System.out.println("convertMainImageQualityToType:"+convertMainImageQualityToType(imageQuality));
		//System.out.println("convertMainImageQualityToSize:"+convertMainImageQualityToSize(imageQuality));
		ImageFormat ifMain = cameraList.get(camnum).getFormatByValues(
				convertMainImageQualityToType(imageQuality),
				convertMainImageQualityToSize(imageQuality)
				);

		ImageFormat ifSec = cameraList.get(camnum).getFormatByValues(
				convertSecondImageQualityToType(imageQuality),
				convertSecondImageQualityToSize(imageQuality)
				);
		
		if(ifMain==null) return 0;
		if(ifSec==null) return ifMain.width;
		return Math.max(ifMain.width,  ifSec.width);

	}

	public static double getPixelY(String productName, int imageQuality) {
		canonUtilsConstruct();
		//find camera
		int camnum=-1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				camnum=i;
				break;
			}
		}
		if(camnum==-1) return -1;
		
		//now convert value to type, size
		ImageFormat ifMain = cameraList.get(camnum).getFormatByValues(
				convertMainImageQualityToType(imageQuality),
				convertMainImageQualityToSize(imageQuality)
				);

		ImageFormat ifSec = cameraList.get(camnum).getFormatByValues(
				convertSecondImageQualityToType(imageQuality),
				convertSecondImageQualityToSize(imageQuality)
				);
		
		if(ifMain==null) return 0;
		if(ifSec==null) return ifMain.height;
		return Math.max(ifMain.height,  ifSec.height);
	}
	
	public static String[] getMainFormatNames(String productName) {
		canonUtilsConstruct();
		
		for (int i = 0; i < cameraList.size(); i++) {
			
			if (productName.equals(cameraList.get(i).productName)) {
				return cameraList.get(i).listMainFormats();
			}
		}
		return null;
	}
	
	
	public static String[] getSecondFormatNames(String productName, String mainFormatName) {
		canonUtilsConstruct();
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				return cameraList.get(i).listSecondFormats(mainFormatName);
			}
		}
		return null;
	}

	public static String[] getCompressFormatsNames(String productName) {
		canonUtilsConstruct();
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				return cameraList.get(i).listCompressFormats();
			}
		}
		return null;
	}


	public static ImageFormat getFormatDetails(String productName, String formatName) {
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				return cameraList.get(i).getFormat(formatName);
			}
		}
		return null;
	}
	
	public static int getCompressQuality(String productName, String compressName) {
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				return cameraList.get(i).getCompressQuality(compressName);
			}
		}
		return -1;
	}
	
	
	public String convertImageSizeToString(int imageSize){
		Field[] fields = ImageSize.class.getFields();
		for(Field field : fields){
			try {
				if(field.getInt(ImageSize.class) == imageSize){
						return field.getName(); 
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public String convertImageTypeToString(int imageType){
		Field[] fields = ImageType.class.getFields();
		for(Field field : fields){
			try {
				if(field.getInt(ImageType.class) == imageType ){
						return field.getName(); 
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public String convertCompressQualityToString(int compressQuality){
		Field[] fields = CompressQuality.class.getFields();
		for(Field field : fields){
			try {
				if(field.getInt(CompressQuality.class) == compressQuality){
						return field.getName(); 
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static int convertMainImageQualityToSize(int imageQuality){
		int res=imageQuality & 0xff000000;
		res = res >> 24;
		return res;
	}
	public static int convertMainImageQualityToType(int imageQuality){
		int res=imageQuality & 0x00f00000;
		res = res >> 20;
		return res;
	}
	public static int convertMainImageQualityToCompress(int imageQuality){
		int res=imageQuality & 0x000f0000;
		res = res >> 16;
		return res;
	}
	public static int convertSecondImageQualityToSize(int imageQuality){
		int res=imageQuality & 0x0000ff00;
		res = res >> 8;
		return res;
	}
	public static int convertSecondImageQualityToType(int imageQuality){
		int res=imageQuality & 0x000000f0;
		res = res >> 4;
		return res;
	}
	public static int convertSecondImageQualityToCompress(int imageQuality){
		int res=imageQuality & 0x0000000f;
		//res = res >> 0;
		return res;
	}
	
	public static int ImageQualityToMainImageIndex(String productName, int imageQuality){
		canonUtilsConstruct();
		//find camera
		
		int camnum=-1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				camnum=i;
				break;
			}
		}
		
		if(camnum==-1) return -1;
		
		int type = convertMainImageQualityToType(imageQuality); 
		int size = convertMainImageQualityToSize(imageQuality);
		
		//now convert value to type, size
		return cameraList.get(camnum).findMainIndexByValues(type, size );
	}
	
	public static int ImageQualityToSecondImageIndex(String productName, String mainFormatName,  int imageQuality){
		canonUtilsConstruct();
		//find camera
		int camnum=-1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				camnum=i;
				break;
			}
		}
		if(camnum==-1) return -1;
		
		//now convert value to type, size
		return cameraList.get(camnum).findSecondIndexByValues(mainFormatName,
				convertSecondImageQualityToType(imageQuality), convertSecondImageQualityToSize(imageQuality));
	}
	
	public static int ImageQualityToCompressIndex(String productName, int imageQuality){
		canonUtilsConstruct();
		//find camera
		int camnum=-1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (productName.equals(cameraList.get(i).productName)) {
				camnum=i;
				break;
			}
		}
		if(camnum==-1) return -1;
		
		//now convert value to type, size
		if(convertMainImageQualityToType(imageQuality)==ImageType.Jpeg)
			return cameraList.get(camnum).findCompressIndexByValues(convertMainImageQualityToCompress(imageQuality));
		else
			return cameraList.get(camnum).findCompressIndexByValues(convertSecondImageQualityToCompress(imageQuality));
	}
	
	public static int mainFormatNameToImageQuality(String productName, String formatName, int imageQuality) {
		ImageFormat img = getFormatDetails(productName, formatName);
		int res = imageQuality & 0x000fffff;
		if(img==null) {
			res |= 0xff000000;
		} else {
			res |= (img.size<<24);
			res |= (img.type<<20);
		}
		return res;
	}
		
	public static int secondFormatNameToImageQuality(String productName, String formatName, int imageQuality) {
		ImageFormat img = getFormatDetails(productName, formatName);
		int res = imageQuality & 0xffff000f;
		if(img==null) {
			res |= 0x0000ff00;
		} else {
			res |= (img.size<<8);
			res |= (img.type<<4);
		}
		return res;
	}
	
	public static int mainCompressNameToImageQuality(String productName, String compressName, int imageQuality) {
		int qual = getCompressQuality(productName, compressName);
		int res = imageQuality & 0xfff0ffff;
		if(qual==-1) {
			res |= 0x000f0000;
		} else {
			res |= (qual<<16);
		}
		return res;
	}
		
	public static int secondCompressNameToImageQuality(String productName, String compressName, int imageQuality) {
		int qual = getCompressQuality(productName, compressName);
		int res = imageQuality & 0xfffffff0;
		if(qual==-1) {
			res |= 0x0000000f;
		} else {
			res |= qual;
		}
		return res;
	}
	
	
	
	
}
