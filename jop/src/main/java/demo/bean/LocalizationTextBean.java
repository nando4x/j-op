package demo.bean;

import java.util.ResourceBundle;

public class LocalizationTextBean {
	private String fileProps;
	private ResourceBundle resourceBundle; 			// proprieta'
	

	/**
	 * @return the fileProps
	 */
	public String getFileProps() {
		return fileProps;
	}

	/**
	 * @param fileProps the fileProps to set
	 */
	public void setFileProps(String fileProps) {
		this.fileProps = fileProps;
		this.resourceBundle = ResourceBundle.getBundle(fileProps);
	}
	
	public String getText(String Name) {
		if ( !this.resourceBundle.containsKey(Name) )
			return Name;
		return this.resourceBundle.getString(Name);
	}
}
