package jonah;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Scanner;
import jonah.Function;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class App extends JPanel {
	private static ArrayList<Integer> parseIndex = new ArrayList<Integer>();
	private static ArrayList<Double> initialX = new ArrayList<Double>();
	private static ArrayList<Double> finalX = new ArrayList<Double>();
	private static ArrayList<double[]> yValues = new ArrayList<double[]>();
	private static JLayeredPane drawerPanel;
	private static ArrayList<String[]> yValuePositions = new ArrayList<String[]>();
	private static ArrayList<int[]> yPointPositions = new ArrayList<int[]>();
	private static ArrayList<String> functionCollection;
	private static Color[] functionColor = {new Color(56, 125, 34), //green
						new Color(15, 61, 148), //blue
						new Color(148, 15, 15), //red
						new Color(222, 211, 38), //yellow
						new Color(201, 109, 22), //orange
						new Color(212, 48, 197), //pink
						new Color(120, 48, 212),};  // purple
	private static double minimumX;
	private static double maximumX;
	private static double minimumY;
	private static double maximumY;
	
	private static PixelGrid grid;
	
	private static Integer prevX;
	private static Integer prevY;
	
	private static Double shiftX;
	private static Double shiftY;
	
	private static double[] xLines;
	private static double[] yLines;
	private static int[] xLinePositions;
	private static int[] yLinePositions;
	
	private static JLabel[] xLabels;
	private static JLabel[] yLabels;
	
	public static void main(String[] args) {
		xLines = new double[24];
		yLines = new double[24];
		xLinePositions = new int[24];
		yLinePositions = new int[24];
		xLines[23] = 0;
		yLines[23] = 0;
		
		shiftX = 0.0;
		shiftY = 0.0;
		
		prevX = null;
		prevY = null;
		
		Scanner userinput = new Scanner(System.in);

		System.out.println("____________________________________________________\n");
		System.out.println("███████╗██╗██╗      █████╗   █████╗██╗      █████╗  ");
		System.out.println("╚════██║██║██║     ██╔══██╗██╔══██╗██║     ██╔══██╗ ");
		System.out.println("  ███╔═╝██║██║     ██║  ╚═╝███████║██║     ██║  ╚═╝ ");
		System.out.println("██╔══╝  ██║██║     ██║  ██╗██╔══██║██║     ██║  ██╗ ");
		System.out.println("███████╗██║███████╗╚█████╔╝██║  ██║███████╗╚█████╔╝ ");
		System.out.println("╚══════╝╚═╝╚══════╝ ╚════╝ ╚═╝  ╚═╝╚══════╝ ╚════╝  ");
		System.out.println("____________________________________________________");

		functionCollection = new ArrayList<String>();
		boolean startGraph = false;
		
		minimumX = -10;
		maximumX = 10;
		minimumY = -10;
		maximumY = 10;
		
		while(!startGraph) {
			System.out.println();
			System.out.println("Input your next Function below: (Type \"GRAPH\" to confirm all functions)");
			System.out.print("y = ");
			String func = "(" + userinput.nextLine() + ")";
			if(func.equalsIgnoreCase("(GRAPH)")) {
				startGraph = true;
			} else {
				functionCollection.add(func);
			}
		}
		JFrame frame = new JFrame("Pixel Grid");
		drawerPanel = new JLayeredPane();
		drawerPanel.setPreferredSize(new Dimension(601, 601));
		
		xLabels = new JLabel[23];
		yLabels = new JLabel[23];
		for(int i = 0; i < 23; i++) {
			xLabels[i] = new JLabel();
			xLabels[i].setVisible(true);
			xLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			xLabels[i].setVerticalAlignment(SwingConstants.CENTER);
			xLabels[i].setOpaque(false);
			drawerPanel.add(xLabels[i], Integer.valueOf(1));
			yLabels[i] = new JLabel();
			yLabels[i].setVisible(true);
			yLabels[i].setHorizontalAlignment(SwingConstants.RIGHT);
			yLabels[i].setVerticalAlignment(SwingConstants.BOTTOM);
			yLabels[i].setOpaque(false);
			drawerPanel.add(yLabels[i], Integer.valueOf(1));
		}
		
		grid = new PixelGrid(functionCollection);
		grid.setBounds(0, 0, 601, 601);
		drawerPanel.add(grid, Integer.valueOf(1));
		grid.setVisible(true);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(drawerPanel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
		
		setUpGraph();
		for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
			graph(functionIndex);
		}
		updateLines(0);
		createLines();
		createLabels();
		
		drawerPanel.setVisible(true);
		
		grid.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == 'i' | e.getKeyChar() == 'o') {
					double lineDiff = 0;
					for(int i = 0; i < 23; i++) {
						if(xLinePositions[i] != -1 && xLinePositions[i] != 601) {
							lineDiff = xLinePositions[i + 1] - xLinePositions[i];
							i = 23;
						}
					}
					if(e.getKeyChar() == 'o') { //zoom out
						deleteLines();
						zoom(-2);
						for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
							graph(functionIndex);
						}
						createLines();
						if(lineDiff < 60) {
							deleteLines();
							updateLines(1);
							createLines();
						}
					}
					if(e.getKeyChar() == 'i') { //zoom in
						deleteLines();
						zoom(+2);
						for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
							graph(functionIndex);
						}
						createLines();
						if(lineDiff > 100) {
							deleteLines();
							updateLines(-1);
							createLines();
						}
					}
					deleteLines();
					fixGraph();
					createLines();
					createLabels();
				}
			}
		});
		grid.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if(e.getX() - prevX != 0) {
					int diff = e.getX() - prevX;
					deleteLines();
					shiftYValues(diff);
					setUpGraph();
					prevX = e.getX();
					for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
						graph(functionIndex);
					}
					createLines();
					double lineDiff = Math.abs(xLines[13] - xLines[12]);
					if(Math.abs(shiftX) > lineDiff) {
						while(Math.abs(shiftX) > lineDiff) {
							deleteLines();
							if(shiftX > 0) {
								for(int i = 0; i < 23; i++) {
									xLines[i] -= lineDiff;
								}
								shiftX -= lineDiff;
							} else if(shiftX < 0) {
								for(int i = 0; i < 23; i++) {
									xLines[i] += lineDiff;
								}
								shiftX += lineDiff;
							}
							createLines();
							createLabels();
						}
					}
				}
				if(e.getY() - prevY != 0) {
					int diff = e.getY() - prevY;
					deleteLines();
					displaceYValues(diff);
					setUpGraph();
					prevY = e.getY();
					for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
						graph(functionIndex);
					}
					createLines();
					double lineDiff = Math.abs(xLines[13] - xLines[12]);
					if(Math.abs(shiftY) > lineDiff) {
						while(Math.abs(shiftY) > lineDiff) {
							deleteLines();
							if(shiftY > 0) {
								for(int i = 0; i < 23; i++) {
									yLines[i] -= lineDiff;
								}
								shiftY -= lineDiff;
							} else if(shiftY < 0) {
								for(int i = 0; i < 23; i++) {
									yLines[i] += lineDiff;
								}
								shiftY += lineDiff;
							}
							createLines();
						}
					}
				}
				createLabels();
			}
		});
		grid.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				grid.setCursor(new Cursor(Cursor.HAND_CURSOR));
				prevX = e.getX();
				prevY = e.getY();
			}
			public void mouseReleased(MouseEvent e) {
				grid.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				prevX = null;
				prevY = null;
			}
		});
		
		grid.addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				double lineDiff = 0;
				for(int i = 0; i < 23; i++) {
					if(xLinePositions[i] != -1 && xLinePositions[i] != 601) {
						lineDiff = xLinePositions[i + 1] - xLinePositions[i];
						i = 23;
					}
				}
				if(e.getWheelRotation() > 0) { //zoom out
					deleteLines();
					zoom(-3);
					for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
						graph(functionIndex);
					}
					createLines();
					if(lineDiff < 60) {
						deleteLines();
						updateLines(1);
						createLines();
					}
				}
				if(e.getWheelRotation() < 0) { //zoom in
					deleteLines();
					zoom(+3);
					for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
						graph(functionIndex);
					}
					createLines();
					if(lineDiff > 100) {
						deleteLines();
						updateLines(-1);
						createLines();
					}
				}
				deleteLines();
				fixGraph();
				createLines();
				createLabels();
			}
		});
		userinput.close();
	}
	
	public static void createLabels() {
		for(int i = 0; i < 23; i++) {
			if(xLinePositions[i] > -1 && xLinePositions[i] < 601) {
				String labelText = formatNumber(xLines[i]);
				xLabels[i].setText(labelText);
				if(yLinePositions[23] > 600) {
					xLabels[i].setBounds(xLinePositions[i] - 25, 4, 50, 20);
					drawRectangle(xLinePositions[i] - 25, 4, 50, 20, new Color(240, 240, 240));
				} else if(yLinePositions[23] < 37) {
					xLabels[i].setBounds(xLinePositions[i] - 25, 575, 50, 20);
					drawRectangle(xLinePositions[i] - 25, 575, 50, 20, new Color(240, 240, 240));
				} else {
					xLabels[i].setBounds(xLinePositions[i] - 25, 611 - yLinePositions[23], 50, 20);
					drawRectangle(xLinePositions[i] - 25, 611 - yLinePositions[23], 50, 20, new Color(240, 240, 240));
				}
				xLabels[i].setVisible(true);
			} else {
				xLabels[i].setVisible(false);
			}
			if(yLinePositions[i] > -1 && yLinePositions[i] < 601) {
				String labelText = formatNumber(yLines[i]);
				yLabels[i].setText(labelText);
				if(xLinePositions[23] > 595) {
					yLabels[i].setBounds(545, 581 - yLinePositions[i], 50, 20);
					drawRectangle(545, 581 - yLinePositions[i], 50, 20, new Color(240, 240, 240));
				} else if(xLinePositions[23] < 50) {
					yLabels[i].setBounds(0, 581 - yLinePositions[i], 50, 20);
					drawRectangle(0, 581 - yLinePositions[i], 50, 20, new Color(240, 240, 240));
				} else {
					yLabels[i].setBounds(xLinePositions[23] - 53, 581 - yLinePositions[i], 50, 20);
					drawRectangle(xLinePositions[23] - 53, 581 - yLinePositions[i], 50, 20, new Color(240, 240, 240));
				}
				yLabels[i].setVisible(true);
			} else {
				yLabels[i].setVisible(false);
			}
		}
	}
	
	public static double round(double num, int amount) {
		double scale = Math.pow(10.0, amount);
		return Math.round(num * scale)/(scale);
	}
	
	public static String formatNumber(double num) {
		String rounded = num + "";
		if(rounded.indexOf("E") != -1) {
			double decimal = Double.parseDouble(rounded.substring(0, rounded.indexOf("E")));
			String exponent = rounded.substring(rounded.indexOf("E") + 1);
			return round(decimal, 4 - exponent.length()) + "E" + exponent;
		}
		if(rounded.substring(rounded.length() - 2).equals(".0")) {
			rounded = rounded.substring(0, rounded.length() - 2);
		} 
		if(rounded.length() > 7) {
			int exponent = 0;
			if(Math.abs(num) > 1) {
				while(Math.abs(num) > 10) {
					num /= 10;
					exponent++;
				}
			} else {
				while(Math.abs(num) < 1) {
					num *= 10;
					exponent--;
				}
			}
			return round(num, 3 - (exponent + "").length()) + "E" + exponent;
		}
		return rounded;
	}
	
	public static void fixGraph() {
		double lineDiff = xLines[13] -xLines[12];
		double maxX = xLines[22] - maximumX;
		double minX = xLines[0] - minimumX;
		double maxY = yLines[22] - maximumY;
		double minY = yLines[0] - minimumY;
		
		while((maxX < 0 && minX < 0) | (maxX > 0 && minX > 0)) {
			if(maxX < 0 && minX < 0) { //if maxX is negative and minX is negative, shift right
				for(int i = 0; i < 23; i++) {
					xLines[i] += lineDiff;
				}
			} else if(maxX > 0 && minX > 0) { //if maxX is positive and minX is positive, shit left
				for(int i = 0; i < 23; i++) {
					xLines[i] -= lineDiff;
				}
			}
			maxX = xLines[22] - maximumX;
			minX = xLines[0] - minimumX;
		}
		while((maxY < 0 && minY < 0) | (maxY > 0 && minY > 0)) {
			if(maxY < 0 && minY < 0) { //if maxX is negative and minX is negative, shift right
				for(int i = 0; i < 23; i++) {
					yLines[i] += lineDiff;
				}
			} else if(maxY > 0 && minY > 0) { //if maxX is positive and minX is positive, shit left
				for(int i = 0; i < 23; i++) {
					yLines[i] -= lineDiff;
				}
			}
			maxY = yLines[22] - maximumY;
			minY = yLines[0] - minimumY;
		}
	}
	
	public static void createLines() {
		double increment = (maximumY - minimumY)/600;
		double firstX = minimumX - increment * 6;
		double firstY = minimumY - increment * 6;
		for(int i = 0; i < 24; i++) {
			xLinePositions[i] = -1;
			yLinePositions[i] = -1;
			double xDiff = Math.abs(xLines[i] - firstX);
			double yDiff = Math.abs(yLines[i] - firstY);
			for(int index = 0;  index < 602; index++) {
				double xVal = minimumX + increment * index;
				double yVal = minimumY + increment * index;
				double initialXDiff = Math.abs(xLines[i] - xVal);
				double initialYDiff = Math.abs(yLines[i] - yVal);
				if(initialXDiff < xDiff) {
					xDiff = initialXDiff;
					xLinePositions[i] = index;
				}
				if(initialYDiff < yDiff) {
					yDiff = initialYDiff;
					yLinePositions[i] = index;
				}
			}
			if(i != 23) {
				drawRectangle(xLinePositions[i], 0, 1, 601, Color.gray);
				drawRectangle(0, 601 - yLinePositions[i], 601, 1, Color.gray);
			} else {
				if(yLinePositions[i] < 601) {
					drawRectangle(0, 601 - yLinePositions[i], 601, 3, Color.black);
				}
				drawRectangle(xLinePositions[i] - 1, 0, 3, 601, Color.black);
			}
		}
	}
	
	public static void deleteLines() {
		for(int i = 0; i < 23; i++) {
			clearRectangle(xLinePositions[i], 0, 1, 601, Color.white);
			clearRectangle(0, 601 - yLinePositions[i], 601, 1, Color.white);
			if(yLinePositions[23] > 600) {
				clearRectangle(xLinePositions[i] - 25, 4, 50, 20, new Color(240, 240, 240));
			} else if(yLinePositions[23] < 37) {
				clearRectangle(xLinePositions[i] - 25, 575, 50, 20, new Color(240, 240, 240));
			} else {
				clearRectangle(xLinePositions[i] - 25, 611 - yLinePositions[23], 50, 20, new Color(240, 240, 240));
			}
			if(xLinePositions[23] > 595) {
				clearRectangle(545, 581 - yLinePositions[i], 50, 20, new Color(240, 240, 240));
			} else if(xLinePositions[23] < 50) {
				clearRectangle(0, 581 - yLinePositions[i], 50, 20, new Color(240, 240, 240));
			} else {
				clearRectangle(xLinePositions[23] - 53, 581 - yLinePositions[i], 50, 20, new Color(240, 240, 240));
			}
		}
		clearRectangle(xLinePositions[23] - 1, 0, 3, 601, Color.white);
		clearRectangle(0, 601 - yLinePositions[23], 601, 3, Color.white);
	}
	
	public static void updateLines(int changeBoundaries) {
		double increment = (maximumX - minimumX)/10;
		double newMinX = minimumX - increment;
		double newMinY = minimumY - increment;
		if(changeBoundaries == 0) {
			for(int i = 0; i < 23; i++) {
				xLines[i] = newMinX + increment * i;
				yLines[i] = newMinY + increment * i;
			}
		} else {
			if(changeBoundaries == 1) {
				for(int i = 0; i < 23; i++) {
					xLines[i] *= 2;
					yLines[i] *= 2;
				}
				shiftX *= 2;
				shiftY *= 2;
			} else {
				for(int i = 0; i < 23; i++) {
					xLines[i] /= 2;
					yLines[i] /= 2;
				}
				shiftX /= 2;
				shiftY /= 2;
			}
		}
	}
	
	public static void drawRectangle(int x, int y, int width, int height, Color color) {
		for(int xinc = x; xinc < x + width; xinc++) {
			for(int yinc = y; yinc < y + height; yinc++) {
				if(xinc > 0 && xinc < 601 && yinc > 0 && yinc < 601) {
					grid.setPixel(xinc, yinc, color, true);
				}
			}
		}
	}
	
	public static void clearRectangle(int x, int y, int width, int height, Color color) {
		for(int xinc = x; xinc < x + width; xinc++) {
			for(int yinc = y; yinc < y + height; yinc++) {
				if(xinc > 0 && xinc < 601 && yinc > 0 && yinc < 601) {
					grid.setPixel(xinc, yinc, color, false);
				}
			}
		}
	}
	
	public static void displaceYValues(int shiftAmount) {
		double increment = (maximumY - minimumY)/600;
		if(shiftAmount > 0) {
			while(shiftAmount > 0) {
				shiftY += -increment;
				maximumY += increment;
				minimumY += increment;
				shiftAmount--;
			}
		}
		if(shiftAmount < 0) {
			while(shiftAmount < 0) {
				shiftY += increment;
				maximumY -= increment;
				minimumY -= increment;
				shiftAmount++;
			}
		}
	}
	
	public static void shiftYValues(int shiftAmount) {
		double increment = (maximumX - minimumX)/600.0;
		if(shiftAmount < 0) { //move function left
			while(shiftAmount < 0) {
				shiftX += -increment;
				parseIndex.clear();
				for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
					for(int i = 1; i < yValues.get(0).length; i++) {
						yValues.get(functionIndex)[i - 1] = yValues.get(functionIndex)[i];
					}
					parseIndex.add(0);
					ArrayList<Object> formula = ParseFunction(functionCollection.get(functionIndex), functionIndex);
					Function tempfunction = new Function(formula);
					yValues.get(functionIndex)[yValues.get(functionIndex).length - 1] = tempfunction.evaluate(maximumX + increment, new ArrayList<Object>(formula), 0);
				}
				minimumX += increment;
				maximumX += increment;
				shiftAmount++;
			}
		}
		if(shiftAmount > 0) { //move function right
			while(shiftAmount > 0) {
				shiftX += increment;
				parseIndex.clear();
				for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
					for(int i = yValues.get(0).length - 1; i > 0; i--) {
						yValues.get(functionIndex)[i] = yValues.get(functionIndex)[i - 1];
					}
					parseIndex.add(0);
					ArrayList<Object> formula = ParseFunction(functionCollection.get(functionIndex), functionIndex);
					Function tempfunction = new Function(formula);
					yValues.get(functionIndex)[0] = tempfunction.evaluate(minimumX - increment, new ArrayList<Object>(formula), 0);
				}
				minimumX -= increment;
				maximumX -= increment;
				shiftAmount--;
			}
		}
	}
	
	public static boolean isWithinPoint(double yVal, double yPoint, double increment) {
		boolean isWithinPoint = false;
		if(Math.abs(yPoint - yVal) <= increment) {
			return true;
		}
		return isWithinPoint;
	}
	
	public static void setUpGraph() {
		parseIndex.clear();
		yValuePositions.clear();
		yPointPositions.clear();
		yValues.clear();
		initialX.clear();
		finalX.clear();
		for(int functionIndex = 0; functionIndex < functionCollection.size(); functionIndex++) {
			for(int x = 0; x < 601; x += 3) {
				for(int y = 0; y < 601; y += 3) {
					grid.setPoint(x, y, Color.white, false, functionIndex);
				}
			}
			parseIndex.add(0);
			yValuePositions.add(new String[601]);
			yPointPositions.add(new int[601]);
			ArrayList<Object> formula = ParseFunction(functionCollection.get(functionIndex), functionIndex);
			Function input = new Function(formula);
			yValues.add(input.findYValues(minimumX, maximumX));
			initialX.add(minimumX);
			finalX.add(maximumX);
		}
	}
	
	public static void zoom(double sign) {
		double increment = sign * (maximumX - minimumX)/100;
		//double increment = sign * 5;
		minimumX += increment;
		maximumX -= increment;
		minimumY += increment;
		maximumY -= increment;
		setUpGraph();
	}
	
	public static void graph(int functionIndex) {
		double range = finalX.get(functionIndex) - initialX.get(functionIndex);
		double increment = (range/600);
		double precision = increment/2;
		double initialY = minimumY;
		
		for(int x = 0; x < 601; x++) {
			double outOfBoundsHigh = maximumY - precision;
			double outOfBoundsLow = minimumY + precision;
			yValuePositions.get(functionIndex)[x] = "OUTSIDE";
			yPointPositions.get(functionIndex)[x] = 0;
			double yVal = initialY;
			if(yValues.get(functionIndex)[x] < outOfBoundsHigh && yValues.get(functionIndex)[x] > outOfBoundsLow) {
				yValuePositions.get(functionIndex)[x] = "INSIDE";
				for(int y = 0; y < 601; y++) {
					yVal += increment;
					if(isWithinPoint(yVal, yValues.get(functionIndex)[x], precision)) {
						CreatePixel(x - 1, y, functionIndex);
						yPointPositions.get(functionIndex)[x] = y;
					}
				}
			}
		}
		fillLines(range, increment, functionIndex, precision);
	}
	
	public static void fillLines(double range, double increment, int functionIndex, double precision) {
		double outOfBoundsHigh = maximumY - precision;
		double outOfBoundsLow = minimumY + precision;
		for(int x = 0; x < 601; x++) {
			boolean hasStarted = false;
			if(!(yValues.get(functionIndex)[x] + "").equals("Infinity") && !(yValues.get(functionIndex)[x] + "").equals("NaN")) {
				if(yValuePositions.get(functionIndex)[x].equals("INSIDE")) {
					if(x != 600) {
						if(yValuePositions.get(functionIndex)[x+1].equals("INSIDE")) {
							int currentY = yPointPositions.get(functionIndex)[x];
							int nextY = yPointPositions.get(functionIndex)[x+1];
							int difference = currentY - nextY;
							int halfway = Math.abs(difference)/2;
							if(difference < 0) { //next point is above
								for(int y = currentY; y < nextY - halfway; y++) {
									CreatePixel(x - 1, y, functionIndex);
								}
								for(int y = nextY - halfway; y < nextY; y++) {
									CreatePixel(x, y, functionIndex);
								}
							}
							if(difference > 0) { //next point is bellow
								for(int y = currentY - 1; y >= currentY - halfway; y--) {
									CreatePixel(x - 1, y, functionIndex);
								}
								for(int y = currentY - halfway - 1; y > nextY; y--) {
									CreatePixel(x, y, functionIndex);
								}
							}
						}
					}
				} else {
					if(x != 600) {
						if(yValuePositions.get(functionIndex)[x+1].equals("INSIDE")) { //outside to inside
							if(yValues.get(functionIndex)[x] > outOfBoundsHigh) { //from the top
								for(int y = 600; y >= 0; y--) {
									if(!hasStarted) {
										if(yPointPositions.get(functionIndex)[x+1] == y) {
											hasStarted = true;
										}
									}
									if(!hasStarted) {
										CreatePixel(x - 1, y, functionIndex);
									}
								}
							} else if(yValues.get(functionIndex)[x] < outOfBoundsLow){ //from the bottom
								for(int y = 0; y < 601; y++) {
									if(!hasStarted) {
										if(yPointPositions.get(functionIndex)[x+1] == y) {
											hasStarted = true;
										}
									} 
									if(!hasStarted) {
										CreatePixel(x - 1, y, functionIndex);
									}
								}
							}
						}
					}
					if(x != 0) {
						if(yValuePositions.get(functionIndex)[x-1].equals("INSIDE")) { //inside to outside
							if(yValues.get(functionIndex)[x] > outOfBoundsHigh) { //from the top
								for(int y = 0; y < 601; y++) {
									if(!hasStarted) {
										if(yPointPositions.get(functionIndex)[x-1] == y) {
											hasStarted = true;
										}
									} else {
										CreatePixel(x - 1, y, functionIndex);
									}
								}
							} else if(yValues.get(functionIndex)[x] < outOfBoundsLow){ //from the bottom
								for(int y = 0; y < 601; y++) {
									if(!hasStarted) {
										if(yPointPositions.get(functionIndex)[x-1] == y) {
											hasStarted = true;
										}
									} 
									if(!hasStarted) {
										CreatePixel(x - 1, y, functionIndex);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static void CreatePixel(int x, int y, int functionIndex) {
		grid.setPoint(x, 600 - y, functionColor[functionIndex % 7], true, functionIndex);
	}
	
	public static ArrayList<Object> ParseFunction(String function, int functionIndex) {
		ArrayList<Object> operation = new ArrayList<Object>();
		String state = "ST";
		int pointer = 1;
		double num = 0;
		double dec = 0;
		int decimalcounter = 0;
		
		while(pointer < function.length()) {
			char tempval = function.charAt(pointer);
			if (tempval == '+' | tempval == '-' | tempval == '*' | tempval == '/' | tempval == '^') {
				if(state.equals("DEC")) {
					num += dec/(Math.pow(10, decimalcounter));
					state = "NUM";
					decimalcounter = 0;
					dec = 0;
				}
				if(state.equals("NUM")) {
					operation.add(num);
					num = 0;
				}  
				operation.add("" + tempval);
				state = "IDLE";
			}
			if(tempval == '.') {
				state = "DEC";
			}
			if(tempval == 's' | tempval == 'c' | tempval == 't' | tempval == 'a' | tempval == 'd' | tempval == 'p' | tempval == 'f' | tempval == 'i') { //All functions
				if(tempval != 'a' | function.charAt(pointer + 1) == 'b') {  //For all functions except arcsin,...,arccot
					operation.add("" + tempval + function.charAt(pointer + 1) + function.charAt(pointer + 2));
					pointer += 2;
                } else {
					String tempadder = "";
					for(int i = 1; i < 6; i++) {
						tempadder += function.charAt(pointer + i);
					}
					operation.add("" + tempval + tempadder);
					pointer += 5;
				}
				state = "IDLE";
			}
            if(tempval == 'l') {
                if(function.charAt(pointer + 1) == 'n') {
                    operation.add("" + tempval + function.charAt(pointer + 1));
                    pointer++;
                } else {
                    operation.add("" + tempval + function.charAt(pointer + 1) + function.charAt(pointer + 2));
                    pointer += 2;
                }
                state = "IDLE";
            }
			if (tempval == ')') {
				if(state.equals("DEC")) {
					num += dec/(Math.pow(10, decimalcounter));
					state = "NUM";
					dec = 0;
					decimalcounter = 0;
				}
				if(state.equals("NUM")) {
					operation.add(num);
					num = 0;
				}
				parseIndex.set(functionIndex, parseIndex.get(functionIndex) + pointer);
				return operation;
			}
			if (tempval == '(') {
				operation.add(ParseFunction(function.substring(pointer), functionIndex));
				pointer += parseIndex.get(functionIndex);
				parseIndex.set(functionIndex, 0);
			}
			if (tempval > 47 && tempval < 58) {
				if(state.equals("DEC")) {
					dec = dec * 10 + Double.parseDouble("" + tempval);
					decimalcounter++;
				} else {
					num = num * 10 + Integer.parseInt("" + tempval);
					state = "NUM";
				}
			}
			if (tempval == 'x' | tempval == 'e' | tempval == 'n') {
				operation.add(tempval);
				state = "IDLE";
			}
			if (tempval == 'p' && function.charAt(pointer + 1) == 'i') {
				operation.add("" + tempval + function.charAt(pointer + 1));
				pointer++;
				state = "IDLE";
			}
			pointer++;
		}
		return operation;
	}

	
}
