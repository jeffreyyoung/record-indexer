package gui;

public interface BatchStateListener {

	void selectedCellChanged(int row, int col);
	
	void valueChanged(int row, int col, String value);
	
}
