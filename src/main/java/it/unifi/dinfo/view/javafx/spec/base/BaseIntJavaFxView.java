package it.unifi.dinfo.view.javafx.spec.base;

import javafx.scene.layout.VBox;

public interface BaseIntJavaFxView {

	VBox createGUI(double width, double height);
	
	void resetGUI();
	
}
