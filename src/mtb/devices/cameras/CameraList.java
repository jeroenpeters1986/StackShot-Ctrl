package mtb.devices.cameras;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mtb.drivers.edsdk.EdsdkHelper;
import mtb.drivers.edsdk.EdsdkHelper.CameraRef;
import mtb.drivers.edsdk.EdsdkHelper.DeviceInfo;
import edsdk.Edsdk;

public class CameraList {
	EdsdkHelper edsdk;
	ArrayList<CameraBase> cameraList = new ArrayList<CameraBase>();
	DecimalFormat nf;
	
	public CameraList(DecimalFormat nf){
		this.nf=nf;
		CameraUser cam = new CameraUser(CameraUser.DRIVER, CameraUser.DRIVER, nf);
		cam.setProductName(CameraUser.DRIVER);
		cam.setKeyID(CameraUser.DRIVER);
		cameraList.add(cam);
	}
	
	public String toString(){
		String str="";
		for (int i = 0; i < cameraList.size(); i++) {
			str+= cameraList.get(i).toString();
		}
		return str;
	}

	// get a String[] constructed with an index of an ArrayList
	public String[] listUserName() {
		String[] list = new String[cameraList.size()];

		for (int i = 0; i < cameraList.size(); i++) {
			list[i] = cameraList.get(i).getUserName();
		}
		return list;
	}

	// sort an ArrayList on index
	public void sort() {
		Collections.sort(cameraList, new Comparator<CameraBase>() {
			public int compare(CameraBase a, CameraBase b) {
				return Integer.signum(a.getUserName()
						.compareTo(b.getUserName()));
			}
		});
	}

	public int size(){
		return cameraList.size();
	}
	
	public int findIndexByUserName(String val) {
		int res = -1;
		for (int i = 0; i < cameraList.size(); i++) {
			if (val.equals(cameraList.get(i).getUserName())) {
				res = i;
				break;
			}
		}
		return res;
	}
	public CameraBase findCameraByUserName(String val) {
		for (int i = 0; i < cameraList.size(); i++) {
			if (val.equals(cameraList.get(i).getUserName())) {
				return cameraList.get(i);
			}
		}
		return null;
	}
	public int findIndexByKeyID(String val) {
		int res = -1;
		for (int i = 0; i < cameraList.size(); i++) {
			System.out.println(i);
			System.out.println(val);
			System.out.println(cameraList.get(i));
			if (val.equals(cameraList.get(i).getKeyID())) {
				res = i;
				break;
			}
		}
		return res;
	}
	public CameraBase findCameraByKeyID(String val) {
		for (int i = 0; i < cameraList.size(); i++) {
			if (val.equals(cameraList.get(i).getKeyID())) {
				return cameraList.get(i);
			}
		}
		return null;
	}
	public String getFirstFreeName(String baseName) {
		int i = 0;
		String str = null;
		do {
			if(i==0) str=baseName;
			else str = baseName +"-"+ i;
			if (findIndexByUserName(str) == -1)
				break;
			i++;
		} while (true);
		return str;

	}

	public void refreshCameraList() {
		if (edsdk == null) {
			try {
				edsdk = new EdsdkHelper();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
		if (edsdk == null)
			return;

		// on mets à 0 les connections
		//System.out.println("BEFORE");
		for (int i = 0; i < cameraList.size(); i++) {
			//System.out.println(cameraList.get(i));
			if (cameraList.get(i).getDriver().equals(EdsdkHelper.DRIVER)) {
				((CameraCanon) cameraList.get(i)).setCameraRef(null);
			}

		}

		ArrayList<CameraRef> list = edsdk.getCameraListTask();
		if (list == null)
			return;
		for (int i = 0; i < list.size(); i++) {
			CameraRef camref = list.get(i);
			//System.out.println("key:"+camref.keyID);
			
			int pos = findIndexByKeyID(camref.keyID);
			//System.out.println("pos:"+pos);
			CameraCanon cam = null;
			if (pos==-1) {
				DeviceInfo deviceInfo = edsdk.getDeviceInfo(list.get(i));
				cam = new CameraCanon(getFirstFreeName(deviceInfo.deviceDescription), EdsdkHelper.DRIVER, nf);
				cameraList.add(cam);
			} else {
				cam = (CameraCanon) cameraList.get(pos);
			}

			cam.setCameraRef(camref);

			edsdk.openSessionTask(list.get(i));
			// System.out.println("\nkEdsPropID_ProductName");

			cam.setProductName(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_ProductName)); // OK

			// System.out.println("\nkEdsPropID_OwnerName");
			cam.setOwnerName(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_OwnerName)); // str NOK

			// System.out.println("\nkEdsPropID_MakerName");
			cam.setMakerName(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_MakerName)); // str NOK

			// System.out.println("\nkEdsPropID_FirmwareVersion");
			cam.setFirmwareVersion(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_FirmwareVersion)); // OK

			// System.out.println("\nkEdsPropID_BatteryLevel");
			cam.setBatteryLevel(edsdk.getPropertyNumber(camref,
					Edsdk.kEdsPropID_BatteryLevel));

			// System.out.println("\nkEdsPropID_SaveTo");
			cam.setSaveTo(edsdk.getPropertyNumber(camref,
					Edsdk.kEdsPropID_SaveTo));// str

			// System.out.println("\nkEdsPropID_CurrentStorage");
			cam.setCurrentStorage(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_CurrentStorage));// ok

			// System.out.println("\nkEdsPropID_CurrentFolder");
			cam.setCurrentFolder(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_CurrentFolder));// ok

			// System.out.println("\nkEdsPropID_BatteryQuality");
			cam.setBatteryQuality(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_BatteryQuality)); // not supported

			// System.out.println("\nkEdsPropID_BodyIDEx");
			cam.setBodyIDEx(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_BodyIDEx)); // not supported

			// System.out.println("\nkEdsPropID_HDDirectoryStructure");
			cam.sethDDirectoryStructure(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_HDDirectoryStructure));// ok

			// System.out.println("\nkEdsPropID_Copyright");
			cam.setCopyright(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_Copyright));

			// System.out.println("\nkEdsPropID_Artist");
			cam.setArtist(edsdk.getPropertyString(camref,
					Edsdk.kEdsPropID_Artist));// ok
			
			//System.out.println("\nkEdsPropID_ProductName");
			cam.setImageQuality(edsdk.getPropertyNumber(camref,
					Edsdk.kEdsPropID_ImageQuality));// ok

			//création de l'ID unique
			cam.setKeyID(camref.keyID);
			
			if(pos==-1) {
				String base=cam.getProductName()+"-"+cam.getKeyID();
				cam.setUserName(getFirstFreeName(base));
			}
			
			//System.out.println(cam);
			// System.out.println("\nkEdsPropID_ProductName");
			edsdk.closeSessionTask(camref);

		}
	sort();
	}


	

	public String getSettings(){
	        String Str="";
	        for(int i=0;i<cameraList.size();i++) {
	        	if(cameraList.get(i).getDriver().equals(EdsdkHelper.DRIVER)) {
		        	Str+=cameraList.get(i).getSettings();
		        	Str+="\t";
	        	}
	        }
	     return Str;
	}
	 
	public void setSettings(String string){
		if(string==null) return;
		String [] list = string.split("\t\t");
		for(int i=0;i<list.length;i++){
			String[] cam = list[i].split("\t");
			if(cam[0].equals(EdsdkHelper.DRIVER)) {
				if(findIndexByUserName(cam[1])==-1){
					CameraCanon nc = new CameraCanon("", "", nf);
					nc.setSettings(list[i]);
					cameraList.add(nc);
				}
			}
		}
	}

	
	
	
	
	
	
	
	
	
	
}
