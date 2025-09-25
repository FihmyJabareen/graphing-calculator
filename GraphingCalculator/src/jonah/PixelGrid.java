package jonah;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PixelGrid extends JPanel {
	private static ArrayList<Color[][]> pixelColors = new ArrayList<Color[][]>();
	private static ArrayList<boolean[][]> pixelVisible = new ArrayList<boolean[][]>();
	private static int functionAmount;
	private static ArrayList<String> functionCollection;
	
	public PixelGrid(ArrayList<String> functionCollections) {
		functionCollection = functionCollections;
		functionAmount = functionCollection.size();
		for(int functionIndex = 0; functionIndex < functionAmount; functionIndex++) {
			pixelColors.add(new Color[601][601]);
			pixelVisible.add(new boolean[601][601]);
			for(int x = 0; x < 601; x++) {
				for(int y = 0; y < 601; y++) {
					pixelColors.get(functionIndex)[x][y] = Color.white;
					pixelVisible.get(functionIndex)[x][y] = false;
				}
			}
		}
		functionAmount++;
		pixelColors.add(new Color[601][601]);
		pixelVisible.add(new boolean[601][601]);
		for(int x = 0; x < 601; x++) {
			for(int y = 0; y < 601; y++) {
				pixelColors.get(pixelColors.size() - 1)[x][y] = Color.white;
				pixelVisible.get(pixelColors.size() - 1)[x][y] = false;
			}
		}
		setOpaque(false);
		setPreferredSize(new Dimension(601, 601));
		setFocusable(true);
		requestFocusInWindow();
	}
	
	public void setPoint(int x, int y, Color color, boolean isVisible, int functionIndex) {
        if(x < 601 && y < 601 && x > -1 && y > -1) {
            pixelColors.get(functionIndex)[x][y] = color;
            pixelVisible.get(functionIndex)[x][y] = isVisible;
		}
		repaint(x, y, 1, 1);
	}
	
	public void setPixel(int x, int y, Color color, boolean showPixel) {
		boolean isVisible = false;
		for(int i = 0; i < functionAmount - 1; i++) {
			if(pixelVisible.get(i)[x][y]) {
				isVisible = true;
                if(color.equals(Color.black)) {
                    Color newColor = pixelColors.get(i)[x][y];
                    int r = newColor.getRed();
                    int g = newColor.getGreen();
                    int b = newColor.getBlue();
                    setPoint(x, y, new Color(r, g, b), true, i);
                }
			}
		}
		if(!isVisible) {
			if(showPixel) {
				pixelColors.get(pixelColors.size() - 1)[x][y] = color;
				pixelVisible.get(pixelColors.size() - 1)[x][y] = true;
			} else {
				pixelColors.get(pixelColors.size() - 1)[x][y] = Color.white;
				pixelVisible.get(pixelColors.size() - 1)[x][y] = false;
			}
			repaint(x, y, 1, 1);
		} else {
			pixelColors.get(pixelColors.size() - 1)[x][y] = Color.white;
			pixelVisible.get(pixelColors.size() - 1)[x][y] = false;
			repaint(x, y, 1, 1);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int functionIndex = 0; functionIndex < functionAmount; functionIndex++) {
			for(int x = 0; x < 601; x++) {
				for(int y = 0; y < 601; y++) {
					if(pixelVisible.get(functionIndex)[x][y]) {
						g.setColor(pixelColors.get(functionIndex)[x][y]);
						g.fillRect(x, y, 1, 1);
					}
				}
			}
		}
	}
}
