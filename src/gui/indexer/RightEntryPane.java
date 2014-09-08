package gui.indexer;

import gui.indexer.htmlviewer.HtmlViewerPane;

import java.awt.GridLayout;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class RightEntryPane extends JTabbedPane implements Serializable {
	
	private JPanel imageNavigation;
	private JPanel fieldHelpPanel;
	private HtmlViewerPane htmlViewerPane;
	
	public RightEntryPane(){
		super();
		
		fieldHelpPanel = new JPanel();
		addTab("Field Help", null, fieldHelpPanel, "Table View");
		
		imageNavigation = new JPanel();
		addTab("Image Navigation", null, imageNavigation, "Form Entry");
		
	}
	
	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

	public JPanel getImageNavigation() {
		return imageNavigation;
	}
	
	public JPanel getFieldHelpPanel() {
		return fieldHelpPanel;
	}

	public HtmlViewerPane getHtmlViewerPane(){
		return htmlViewerPane;
	}
	
	public void addHtmlPane(HtmlViewerPane htmlViewerPane_in) {
		this.htmlViewerPane = htmlViewerPane_in;
		getFieldHelpPanel().add(htmlViewerPane);
	}

}
