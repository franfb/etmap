package etp.modelo;

import java.util.List;

import ncsa.hdf.object.*;

public class ModisLoader {
	private FileFormat testFile;
	public static final int LST_DAY_1KM = 0;
	public static final int LST_NIGHT_1KM = 4;
	
	public void openFile(String fname) throws Exception {
		// retrieve an instance of H4File
		FileFormat fileFormat = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF4);

		if (fileFormat == null) {
			System.err.println("Cannot find HDF4 FileFormat.");
			return;
		}

		// open the file with read-only access
		testFile = fileFormat.open(fname, FileFormat.READ);

		if (testFile == null) {
			System.err.println("Failed to open file: " + fname);
			return;
		}
		
		testFile.open();
	}
	
	public void closeFile() throws Exception{
		if (testFile != null) testFile.close();
		testFile = null;
	}
	
	public void printAttribute(Integer member) throws Exception {
		if (testFile == null) {
			System.err.println("The file " + testFile.getName() + " is not opened.");
			return;
		}

		// open the file and retrieve the file structure
		Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) testFile
				.getRootNode()).getUserObject();

		System.out.println("Atributos del grupo ra�z");
		printAttributeList(root.getMetadata());
		
		Group group0 = (Group) root.getMemberList().get(0);
		System.out.println("Atributos del grupo MODIS_Grid_Daily_1km_LST");
		printAttributeList(group0.getMetadata());
		
		Group group1 = (Group) group0.getMemberList().get(0);
		System.out.println("Atributos del grupo Data Fields");
		printAttributeList(group0.getMetadata());
		
		Dataset dataset = (Dataset) group1.getMemberList().get(member);
		System.out.println("Atributos del dataset LST_Day_1km");
		printAttributeList(dataset.getMetadata());
	}
	
	public void printAttributeList(List attrList) {
		for (int i = 0; i < attrList.size(); i++) {
			Attribute attr = (Attribute) attrList.get(i);
			//attr.t
			// print out attribute value
			System.out.println(attr.toString() + " = ");
			System.out.println(attr.toString(" ---- "));
		}
	}
	
	public void readDataset(LstData data, int datasetNumber) throws Exception {
		if (testFile == null) {
			System.err.println("The file is not opened.");
			return;
		}

		// retrieve the file structure
		Group group = (Group) ((javax.swing.tree.DefaultMutableTreeNode) testFile
				.getRootNode()).getUserObject();
		group = (Group) group.getMemberList().get(0);
		group = (Group) group.getMemberList().get(0);
		
		Dataset dataset = (Dataset) group.getMemberList().get(datasetNumber);
		short[] data2 = (short[])dataset.read();
		data.setData(data2);
	}

	public void printFileStructure(String fname) throws Exception {
		// retrieve an instance of H4File
		FileFormat fileFormat = FileFormat
				.getFileFormat(FileFormat.FILE_TYPE_HDF4);

		if (fileFormat == null) {
			System.err.println("Cannot find HDF4 FileFormat.");
			return;
		}

		// open the file with read-only access
		FileFormat testFile = fileFormat.open(fname, FileFormat.READ);

		if (testFile == null) {
			System.err.println("Failed to open file: " + fname);
			return;
		}

		// open the file and retrieve the file structure
		testFile.open();
		Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) testFile
				.getRootNode()).getUserObject();

		printGroup(root, "");

		// close file resource
		testFile.close();
	}

	/**
	 * Recursively print a group and its members.
	 * 
	 * @throws Exception
	 */
	private static void printGroup(Group g, String indent) throws Exception {
		if (g == null)
			return;

		java.util.List members = g.getMemberList();

		int n = members.size();
		indent += "    ";
		HObject obj = null;
		for (int i = 0; i < n; i++) {
			obj = (HObject) members.get(i);
			System.out.println(indent + obj);
			if (obj instanceof Group) {
				printGroup((Group) obj, indent);
			}
		}
	}
}
