package pick.part.src;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import pick.part.FileIO.*;

public class FileViewer extends Canvas implements Runnable, KeyListener, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener {
	Toolkit tk = Toolkit.getDefaultToolkit();
    public int WIDTH2 = ((int) tk.getScreenSize().getWidth());
    public int HEIGHT2 = ((int) tk.getScreenSize().getHeight());
    
    public int WIDTH = 400;
    public int HEIGHT = 400;
    
    public boolean teacherOrg = false;
    public boolean gradeOrg = true;
    public boolean dateOrg = false;
    
    Main main;
    
    public int offset = 0;
    
    public long lastUpdate = System.currentTimeMillis();
    
    public static final String NAME = "File Viewer";
    DecimalFormat df = new DecimalFormat("#.##");
    private JFrame jFrame;
    public static Font font = new Font("Calibri", Font.BOLD, 15);
    ArrayList<Filum> fileArray = new ArrayList<Filum>();

    boolean toReparse = false;
    
    public int off = 0;
    
    public void load(Save save) {
    	save.load();
    }
    
    public void loadFiles() {
    	main.usingSaves = new ArrayList<Save>();
    	System.gc();
    	if(fileArray.get(0) instanceof Save) {
        	for(int i = 0; i < fileArray.size(); i++) {
        		if(((Save)fileArray.get(i)).selected) {
        			load(((Save)fileArray.get(i)));
        			main.usingSaves.add(((Save)fileArray.get(i)));
        		}
        	}
        }
        if(fileArray.get(0) instanceof IDList) {
        	for(int i = 0; i < ((IDList)fileArray.get(0)).containers.size(); i++) {
        		if(((IDList)fileArray.get(0)).containers.get(0) instanceof SaveList) {
        			for(int j = 0; j < ((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.size(); j++) {
        				if(((Save)((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j)).selected) {
                			load(((Save)((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j)));
                			main.usingSaves.add(((Save)((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j)));
                		}
        			}
        		}
        		else {
        			for(int k = 0; k < ((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.size(); k++) {
        				if(((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(0) instanceof SaveList) {
                			for(int j = 0; j < ((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.size(); j++) {
                				if(((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)).selected) {
                        			load(((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)));
                        			main.usingSaves.add(((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)));
                        		}
                			}
                		}
                		else {
                			for(int l = 0; l < ((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.size(); l++) {
                				if(((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(0) instanceof SaveList) {
                        			for(int j = 0; j < ((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.size(); j++) {
                        				if(((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected) {
                                			load(((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)));
                                			main.usingSaves.add(((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)));
                                		}
                        			}
                        		}
                        	}
                		}
                	}
        		}
        	}
        }
    }
    
    public void parseFiles(File file) {
    	fileArray = new ArrayList<Filum>();
    	System.gc();
    	File[] files = file.listFiles();
    	ArrayList<File> folders = new ArrayList<File>();
    	for(int i = 0; i < files.length; i++) {
    		if(files[i].getName().contains("x")) {
    			folders.add(files[i]);
    			//System.out.println(files[i]);
    		}
    	}
    	if(teacherOrg) {
			if(gradeOrg) {
				if(dateOrg) {
					fileArray.add(new IDList());
					for(int i = 0; i < folders.size(); i++) {
						File savesIn = new File(folders.get(i).getName());
						String currentTeacher = parseTeacher(folders.get(i).getName());
						//System.out.println("-----" + currentTeacher + "-----");
						boolean toCreate = true;
						int createAtIndex = -1;
						for(int z = 0; z < ((IDList)fileArray.get(0)).id.size(); z++) {
							//System.out.println("=====" + currentTeacher + "vs" + ((IDList)fileArray.get(0)).id.get(z) + "=====");
							if(currentTeacher.equals(((IDList)fileArray.get(0)).id.get(z))) {
								toCreate = false;
								createAtIndex = z;
							}
						}
						if(toCreate) {
							((IDList)fileArray.get(0)).id.add(currentTeacher);
							((IDList)fileArray.get(0)).containers.add(new IDList());
							createAtIndex = ((IDList)fileArray.get(0)).containers.size() - 1;
						}
						
						String currentGrade = parseGradeFromFolder(folders.get(i).getName());
						//System.out.println("ddddd" + currentGrade + "ddddd");
						toCreate = true;
						int createAtSecondIndex = -1;
						for(int z = 0; z < ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.size(); z++) {
							//System.out.println("=====" + currentGrade + "vs" + ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.get(z) + "=====");
							if(currentGrade.equals(((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.get(z))) {
								toCreate = false;
								createAtSecondIndex = z;
							}
						}
						if(toCreate) {
							((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.add(currentGrade);
							((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.add(new IDList());
							createAtSecondIndex = ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.size() - 1;
						}
						
						//System.out.println("cccccc" + createAtIndex);
						
						String currentDate = parseDate(folders.get(i).getName());
						//System.out.println("leedle" + currentDate + "leedle");
						toCreate = true;
						
						for(int z = 0; z < ((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(createAtSecondIndex)).id.size(); z++) {
							//System.out.println("=====" + currentDate + "vs" + ((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(createAtSecondIndex)).id.get(z) + "=====");
							if(currentDate.equals(((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(createAtSecondIndex)).id.get(z))) {
								toCreate = false;
							}
						}
						if(toCreate) {
							((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(createAtSecondIndex)).id.add(currentDate);
							((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(createAtSecondIndex)).containers.add(new SaveList());
						}
						
						//Find the right date to insert files
						File[] savesIns = savesIn.listFiles();
			    		for(int j = 0; j < savesIns.length; j++) {
			    			if(savesIns[j].getName().charAt(0) != '.') {
			    				//System.out.println(savesIns[j].getName());
			    				//System.out.println(parseGrade(savesIns[j].getName()));
			    				//System.out.println(((IDList)fileArray.get(0)).id.get(((IDList)fileArray.get(0)).containers.size() - 1));
			    				((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(createAtSecondIndex)).containers.get(((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(createAtSecondIndex)).containers.size() - 1)).saves.add(new Save(savesIns[j].getPath(), savesIns[j].getName()));
			    			}
			    		}
					}
				}
				else {
					fileArray.add(new IDList());
					for(int i = 0; i < folders.size(); i++) {
						File savesIn = new File(folders.get(i).getName());
						String currentTeacher = parseTeacher(folders.get(i).getName());
						//System.out.println("-----" + currentTeacher + "-----");
						boolean toCreate = true;
						int createAtIndex = -1;
						for(int z = 0; z < ((IDList)fileArray.get(0)).id.size(); z++) {
							//System.out.println("=====" + currentTeacher + "vs" + ((IDList)fileArray.get(0)).id.get(z) + "=====");
							if(currentTeacher.equals(((IDList)fileArray.get(0)).id.get(z))) {
								toCreate = false;
								createAtIndex = z;
								//System.out.println("SET!!!");
							}
						}
						if(toCreate) {
							((IDList)fileArray.get(0)).id.add(currentTeacher);
							((IDList)fileArray.get(0)).containers.add(new IDList());
							createAtIndex = ((IDList)fileArray.get(0)).containers.size() - 1;
						}
						
						String currentGrade = parseGradeFromFolder(folders.get(i).getName());
						//System.out.println("ddddd" + currentGrade + "ddddd");
						toCreate = true;
						
						for(int z = 0; z < ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.size(); z++) {
							//System.out.println("=====" + currentGrade + "vs" + ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.get(z) + "=====");
							if(currentGrade.equals(((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.get(z))) {
								toCreate = false;
							}
						}
						if(toCreate) {
							((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.add(currentGrade);
							((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.add(new SaveList());
						}
						
						//System.out.println("cccccc" + createAtIndex);
						
						//Find the right date to insert files
						File[] savesIns = savesIn.listFiles();
			    		for(int j = 0; j < savesIns.length; j++) {
			    			if(savesIns[j].getName().charAt(0) != '.') {
			    				//System.out.println(savesIns[j].getName());
			    				//System.out.println(parseGrade(savesIns[j].getName()));
			    				//System.out.println(((IDList)fileArray.get(0)).id.get(((IDList)fileArray.get(0)).containers.size() - 1));
			    				((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.size() - 1)).saves.add(new Save(savesIns[j].getPath(), savesIns[j].getName()));
			    			}
			    		}
					}
				}
			}
			else {
				if(dateOrg) {
					fileArray.add(new IDList());
					for(int i = 0; i < folders.size(); i++) {
						File savesIn = new File(folders.get(i).getName());
						String currentTeacher = parseTeacher(folders.get(i).getName());
						//System.out.println("-----" + currentTeacher + "-----");
						boolean toCreate = true;
						int createAtIndex = -1;
						for(int z = 0; z < ((IDList)fileArray.get(0)).id.size(); z++) {
							//System.out.println("=====" + currentTeacher + "vs" + ((IDList)fileArray.get(0)).id.get(z) + "=====");
							if(currentTeacher.equals(((IDList)fileArray.get(0)).id.get(z))) {
								toCreate = false;
								createAtIndex = z;
								//System.out.println("SET!!!");
							}
						}
						if(toCreate) {
							((IDList)fileArray.get(0)).id.add(currentTeacher);
							((IDList)fileArray.get(0)).containers.add(new IDList());
							createAtIndex = ((IDList)fileArray.get(0)).containers.size() - 1;
						}
						
						String currentDate = parseDate(folders.get(i).getName());
						//System.out.println("ddddd" + currentDate + "ddddd");
						toCreate = true;
						
						for(int z = 0; z < ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.size(); z++) {
							//System.out.println("=====" + currentDate + "vs" + ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.get(z) + "=====");
							if(currentDate.equals(((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.get(z))) {
								toCreate = false;
							}
						}
						if(toCreate) {
							((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.add(currentDate);
							((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.add(new SaveList());
						}
						
						//System.out.println("cccccc" + createAtIndex);
						
						//Find the right date to insert files
						File[] savesIns = savesIn.listFiles();
			    		for(int j = 0; j < savesIns.length; j++) {
			    			if(savesIns[j].getName().charAt(0) != '.') {
			    				//System.out.println(savesIns[j].getName());
			    				//System.out.println(parseGrade(savesIns[j].getName()));
			    				//System.out.println(((IDList)fileArray.get(0)).id.get(((IDList)fileArray.get(0)).containers.size() - 1));
			    				((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.size() - 1)).saves.add(new Save(savesIns[j].getPath(), savesIns[j].getName()));
			    			}
			    		}
					}
				}
				else {
					fileArray.add(new IDList());
					for(int i = 0; i < folders.size(); i++) {
						File savesIn = new File(folders.get(i).getName());
						String currentDate = parseTeacher(folders.get(i).getName());
						//System.out.println("-----" + currentDate + "-----");
						boolean toCreate = true;
						for(int z = 0; z < ((IDList)fileArray.get(0)).id.size(); z++) {
							//System.out.println("=====" + currentDate + "vs" + ((IDList)fileArray.get(0)).id.get(z) + "=====");
							if(currentDate.equals(((IDList)fileArray.get(0)).id.get(z))) {
								toCreate = false;
							}
						}
						if(toCreate) {
							((IDList)fileArray.get(0)).id.add(currentDate);
							((IDList)fileArray.get(0)).containers.add(new SaveList());
						}
						//Find the right date to insert files
						File[] savesIns = savesIn.listFiles();
			    		for(int j = 0; j < savesIns.length; j++) {
			    			if(savesIns[j].getName().charAt(0) != '.') {
			    				//System.out.println(savesIns[j].getName());
			    				//System.out.println(parseGrade(savesIns[j].getName()));
			    				//System.out.println(((IDList)fileArray.get(0)).id.get(((IDList)fileArray.get(0)).containers.size() - 1));
			    				((SaveList)((IDList)fileArray.get(0)).containers.get(((IDList)fileArray.get(0)).containers.size() - 1)).saves.add(new Save(savesIns[j].getPath(), savesIns[j].getName()));
			    			}
			    		}
					}
				}
			}
		}
		else {
			if(gradeOrg) {
				if(dateOrg) {
					fileArray.add(new IDList());
					for(int i = 0; i < folders.size(); i++) {
						File savesIn = new File(folders.get(i).getName());
						String currentGrade = parseGradeFromFolder(folders.get(i).getName());
						//System.out.println("-----" + currentGrade + "-----");
						boolean toCreate = true;
						int createAtIndex = -1;
						for(int z = 0; z < ((IDList)fileArray.get(0)).id.size(); z++) {
							//System.out.println("=====" + currentGrade + "vs" + ((IDList)fileArray.get(0)).id.get(z) + "=====");
							if(currentGrade.equals(((IDList)fileArray.get(0)).id.get(z))) {
								toCreate = false;
								createAtIndex = z;
								//System.out.println("SET!!!");
							}
						}
						if(toCreate) {
							((IDList)fileArray.get(0)).id.add(currentGrade);
							((IDList)fileArray.get(0)).containers.add(new IDList());
							createAtIndex = ((IDList)fileArray.get(0)).containers.size() - 1;
						}
						
						String currentDate = parseDate(folders.get(i).getName());
						//System.out.println("ddddd" + currentDate + "ddddd");
						toCreate = true;
						
						for(int z = 0; z < ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.size(); z++) {
							//System.out.println("=====" + currentDate + "vs" + ((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.get(z) + "=====");
							if(currentDate.equals(((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.get(z))) {
								toCreate = false;
							}
						}
						if(toCreate) {
							((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).id.add(currentDate);
							((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.add(new SaveList());
						}
						
						//System.out.println("cccccc" + createAtIndex);
						
						//Find the right date to insert files
						File[] savesIns = savesIn.listFiles();
			    		for(int j = 0; j < savesIns.length; j++) {
			    			if(savesIns[j].getName().charAt(0) != '.') {
			    				//System.out.println(savesIns[j].getName());
			    				//System.out.println(parseGrade(savesIns[j].getName()));
			    				//System.out.println(((IDList)fileArray.get(0)).id.get(((IDList)fileArray.get(0)).containers.size() - 1));
			    				((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.get(((IDList)((IDList)fileArray.get(0)).containers.get(createAtIndex)).containers.size() - 1)).saves.add(new Save(savesIns[j].getPath(), savesIns[j].getName()));
			    			}
			    		}
					}
				}
				else {
					fileArray.add(new IDList());
					for(int i = 0; i < folders.size(); i++) {
						File savesIn = new File(folders.get(i).getName());
						String currentDate = parseGradeFromFolder(folders.get(i).getName());
						//System.out.println("-----" + currentDate + "-----");
						boolean toCreate = true;
						for(int z = 0; z < ((IDList)fileArray.get(0)).id.size(); z++) {
							//System.out.println("=====" + currentDate + "vs" + ((IDList)fileArray.get(0)).id.get(z) + "=====");
							if(Integer.parseInt(currentDate) == Integer.parseInt(((IDList)fileArray.get(0)).id.get(z))) {
								toCreate = false;
							}
						}
						if(toCreate) {
							((IDList)fileArray.get(0)).id.add(currentDate);
							((IDList)fileArray.get(0)).containers.add(new SaveList());
						}
						//Find the right date to insert files
						File[] savesIns = savesIn.listFiles();
			    		for(int j = 0; j < savesIns.length; j++) {
			    			if(savesIns[j].getName().charAt(0) != '.') {
			    				//System.out.println(savesIns[j].getName());
			    				//System.out.println(parseGrade(savesIns[j].getName()));
			    				//System.out.println(((IDList)fileArray.get(0)).id.get(((IDList)fileArray.get(0)).containers.size() - 1));
			    				((SaveList)((IDList)fileArray.get(0)).containers.get(((IDList)fileArray.get(0)).containers.size() - 1)).saves.add(new Save(savesIns[j].getPath(), savesIns[j].getName()));
			    			}
			    		}
					}
				}
			}
			else {
				if(dateOrg) {
					fileArray.add(new IDList());
					for(int i = 0; i < folders.size(); i++) {
						File savesIn = new File(folders.get(i).getName());
						String currentDate = parseDate(folders.get(i).getName());
						//System.out.println("-----" + currentDate + "-----");
						boolean toCreate = true;
						for(int z = 0; z < ((IDList)fileArray.get(0)).id.size(); z++) {
							if(currentDate.equals(((IDList)fileArray.get(0)).id.get(z))) {
								toCreate = false;
							}
						}
						if(toCreate) {
							((IDList)fileArray.get(0)).id.add(currentDate);
							((IDList)fileArray.get(0)).containers.add(new SaveList());
						}
						//Find the right date to insert files
						File[] savesIns = savesIn.listFiles();
			    		for(int j = 0; j < savesIns.length; j++) {
			    			if(savesIns[j].getName().charAt(0) != '.') {
			    				//System.out.println(savesIns[j].getName());
			    				//System.out.println(parseGrade(savesIns[j].getName()));
			    				//System.out.println(((IDList)fileArray.get(0)).id.get(((IDList)fileArray.get(0)).containers.size() - 1));
			    				((SaveList)((IDList)fileArray.get(0)).containers.get(((IDList)fileArray.get(0)).containers.size() - 1)).saves.add(new Save(savesIns[j].getPath(), savesIns[j].getName()));
			    			}
			    		}
					}
				}
				else {
					for(int i = 0; i < folders.size(); i++) {
			    		File savesIn = new File(folders.get(i).getName());
			    		File[] savesIns = savesIn.listFiles();
			    		for(int j = 0; j < savesIns.length; j++) {
			    			if(savesIns[j].getName().charAt(0) != '.') {
			    				//System.out.println(savesIns[j].getName());
			    				//System.out.println(parseGrade(savesIns[j].getName()));
			    				fileArray.add(new Save(savesIns[j].getPath(), savesIns[j].getName()));
			    			}
			    		}
			    	}
				}
			}
		}
    }
    
    public static String parseGrade(String name) {
    	char c = name.charAt(0);
    	boolean isDigit = (c >= '0' && c <= '9');
    	if(isDigit) {
    		return (name.charAt(0) + "");
    	}
    	else {
    		return null;
    	}
    }
    public static String parseAttempt(String name) {
    	char c = name.charAt(1);
    	boolean isDigit = (c >= '0' && c <= '9');
    	if(isDigit) {
    		return (name.charAt(1) + "");
    	}
    	else {
    		return null;
    	}
    }
    public static String parseName(String name) {
    	String namn = "";
    	for(int i = 0; i < name.length() - 6; i++) {
			char c = name.charAt(i);
        	boolean isDigit = (c >= '0' && c <= '9');
        	if(!isDigit) {
        		if(name.charAt(i) != '/' && name.charAt(i) != '.') {
        			if(name.charAt(i) == '_') {
        				namn += " ";
        			}
        			else {
        				namn += name.charAt(i);
        			}
        		}
        	}
		}
    	return namn;
    }
    
    public static String parseDate(String name) {
    	String[] tokens = name.split("x");
    	//System.out.println(tokens[3]);
    	return tokens[3];
    }
    public static String parseGradeFromFolder(String name) {
    	String[] tokens = name.split("x");
    	//System.out.println(tokens[2]);
    	return tokens[2];
    }
    public static String parseTeacher(String name) {
    	String[] tokens = name.split("x");
    	//System.out.println(tokens[1]);
    	return tokens[1];
    }
    
    public int findIndexByID(ArrayList<String> id, String toFind) {
    	for(int i = 0; i < id.size(); i++) {
    		if(toFind == id.get(i)) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    public FileViewer(Main m) {
    	main = m;
    	
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));

        jFrame = new JFrame(NAME);
        //jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        jFrame.add(this, BorderLayout.CENTER);
        jFrame.setResizable(true);
        jFrame.setLocation(WIDTH2/2 - WIDTH/2,HEIGHT2/2 - HEIGHT/2);
        //jFrame.setUndecorated(true);
        jFrame.setVisible(true);
        jFrame.addKeyListener(this);
        addKeyListener(this);
        jFrame.addMouseListener(this);
        addMouseListener(this);
        jFrame.addMouseMotionListener(this);
        addMouseMotionListener(this);
        jFrame.addMouseWheelListener(this);
        addMouseWheelListener(this);
        jFrame.pack();
        parseFiles(new File("."));
    }

    public void drawCircle(Graphics g, double x, double y, int r) {
    	g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
    }
    
	public void run() {
		while (true) {
            //System.out.println("Hej");
			if(toReparse){
				parseFiles(new File("."));
				toReparse = false;
			}
        	tick();
        	render();
            /*try {
                Thread.sleep(0);
            } catch (Exception e) {
            }*/
		}
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setFont(font);
        
        g.drawRect(10, 30, 380, 358);
        
        if(fileArray.get(0) instanceof Save) {
        	for(int i = 0; i < fileArray.size(); i++) {
        		g.setColor(Color.BLACK);
        		g.drawRect(20, 40 + 20*i + offset, 10, 10);
        		g.drawString("" + parseName(((Save)fileArray.get(i)).name), 40, 50 + 20*i + offset);
        		if(((Save)fileArray.get(i)).selected) {
        			g.setColor(new Color(128, 128, 255));
        			g.fillRect(21, 41 + 20*i + offset, 8, 8);
        		}
        	}
        }
        g.setColor(Color.BLACK);
        if(fileArray.get(0) instanceof IDList) {
        	off = 0;
        	for(int i = 0; i < ((IDList)fileArray.get(0)).containers.size(); i++) {
        		g.setColor(Color.BLACK);
        		g.drawRect(20, 40 + 20*off + offset, 10, 10);
        		g.drawString("" + ((IDList)fileArray.get(0)).id.get(i), 40, 50 + 20*off + offset);
        		off++;
        		if(((IDList)fileArray.get(0)).containers.get(0) instanceof SaveList) {
        			for(int j = 0; j < ((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.size(); j++) {
        				g.setColor(Color.BLACK);
        				g.drawRect(50, 40 + 20*off + offset, 10, 10);
                		g.drawString("" + parseName(((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j).name), 70, 50 + 20*off + offset);
                		if(((Save)((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j)).selected) {
                			g.setColor(new Color(128, 128, 255));
                			g.fillRect(51, 41 + 20*off + offset, 8, 8);
                		}
                		off++;
        			}
        		}
        		else {
        			for(int k = 0; k < ((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.size(); k++) {
        				g.setColor(Color.BLACK);
        				g.drawRect(50, 40 + 20*off + offset, 10, 10);
                		g.drawString("" + ((IDList)((IDList)fileArray.get(0)).containers.get(i)).id.get(k), 70, 50 + 20*off + offset);
                		off++;
                		if(((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(0) instanceof SaveList) {
                			for(int j = 0; j < ((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.size(); j++) {
                				g.setColor(Color.BLACK);
                				g.drawRect(80, 40 + 20*off + offset, 10, 10);
                        		g.drawString("" + parseName(((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j).name), 100, 50 + 20*off + offset);
                        		if(((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)).selected) {
                        			g.setColor(new Color(128, 128, 255));
                        			g.fillRect(81, 41 + 20*off + offset, 8, 8);
                        		}
                        		off++;
                			}
                		}
                		else {
                			for(int l = 0; l < ((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.size(); l++) {
                				g.setColor(Color.BLACK);
                				g.drawRect(80, 40 + 20*off + offset, 10, 10);
                        		g.drawString("" + ((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).id.get(l), 100, 50 + 20*off + offset);
                        		off++;
                        		if(((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(0) instanceof SaveList) {
                        			for(int j = 0; j < ((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.size(); j++) {
                        				g.setColor(Color.BLACK);
                        				g.drawRect(110, 40 + 20*off + offset, 10, 10);
                                		g.drawString("" + parseName(((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j).name), 130, 50 + 20*off + offset);
                                		if(((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected) {
                                			g.setColor(new Color(128, 128, 255));
                                			g.fillRect(111, 41 + 20*off + offset, 8, 8);
                                		}
                                		off++;
                        			}
                        		}
                        	}
                		}
                	}
        		}
        	}
        }
        
        g.clearRect(0, 0, WIDTH, 30);
        g.clearRect(0, HEIGHT - 12, WIDTH, 12);
        
        /*g.setColor(Color.RED);
        g.fillRect(0, 0, 110, 30);
        
        g.setColor(Color.GREEN);
        g.fillRect(110, 0, 100, 30);
        
        g.setColor(Color.BLUE);
        g.fillRect(210, 0, 90, 30);*/
        
        g.setColor(Color.GRAY);
        g.fillRect(300, 0, 100, 30);
        
        g.setColor(Color.BLACK);
        
        g.drawString("Load Files", 310, 20);
        
        g.drawRect(10, 10, 10, 10);
        g.drawString("Teacher", 23, 20);
        
        g.drawRect(110, 10, 10, 10);
        g.drawString("Grade", 123, 20);
        
        g.drawRect(210, 10, 10, 10);
        g.drawString("Date", 223, 20);
        
        g.setColor(new Color(128, 128, 255));
        if(teacherOrg) {
        	g.fillRect(11, 11, 8, 8);
        }
        if(gradeOrg) {
        	g.fillRect(111, 11, 8, 8);
        }
        if(dateOrg) {
        	g.fillRect(211, 11, 8, 8);
        }
        
        g.dispose();
        bs.show();
	}

	private void tick() {
		if(offset > 0) {
			offset = 0;
		}
		if(!teacherOrg && !gradeOrg && !dateOrg) {
			if(offset + 20*fileArray.size() + 30 < 380) {
				offset = 380 - 20*fileArray.size() - 30;
			}
		}
		else {
			if(offset + 20*off + 30 < 380) {
				offset = 380 - 20*off - 30;
			}
		}
	}
	
	/*public static void main(String args[]) {
        new FileViewer().start();
    }*/
    
	public void start() {
		new Thread(this).start();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int mouseX = e.getX();
		int mouseY = e.getY();
		if(mouseX > 0 && mouseX < 110) {
			if(mouseY > 0 && mouseY < 30) {
				teacherOrg = !teacherOrg;
				toReparse = true;
			}
		}
		if(mouseX > 110 && mouseX < 210) {
			if(mouseY > 0 && mouseY < 30) {
				gradeOrg = !gradeOrg;
				toReparse = true;
			}
		}
		if(mouseX > 210 && mouseX < 300) {
			if(mouseY > 0 && mouseY < 30) {
				dateOrg = !dateOrg;
				toReparse = true;
			}
		}
		if(mouseX > 300 && mouseX < 400) {
			if(mouseY > 0 && mouseY < 30) {
				loadFiles();
				for(int i = 0; i < main.usingSaves.size(); i++) {
					System.out.println(main.usingSaves.get(i).name);
				}
			}
		}
		if(fileArray.get(0) instanceof Save) {
        	for(int i = 0; i < fileArray.size(); i++) {
        		if(mouseX > 10 && mouseX < 390) {
        			if(mouseY > 30 && mouseY > 40 + 20*i + offset && mouseY < 50 + 20*i + offset) {
        				((Save)fileArray.get(i)).selected = !((Save)fileArray.get(i)).selected;
        			}
        		}
        	}
        }
		if(fileArray.get(0) instanceof IDList) {
        	off = 0;
        	for(int i = 0; i < ((IDList)fileArray.get(0)).containers.size(); i++) {
        		if(mouseX > 10 && mouseX < 390) {
        			if(mouseY > 30 && mouseY > 40 + 20*off + offset && mouseY < 50 + 20*off + offset) {
        				int beginOff = off;
        				//BEGIN
        				if(((IDList)fileArray.get(0)).containers.get(0) instanceof SaveList) {
                			for(int j = 0; j < ((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.size(); j++) {
                        		((Save)((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j)).selected = !((Save)((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j)).selected;
                			}
                		}
                		else {
                			for(int k = 0; k < ((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.size(); k++) {
                				if(((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(0) instanceof SaveList) {
                        			for(int j = 0; j < ((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.size(); j++) {
                                		((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)).selected = !((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)).selected;
                        			}
                        		}
                        		else {
                        			for(int l = 0; l < ((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.size(); l++) {
                        				if(((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(0) instanceof SaveList) {
                                			for(int j = 0; j < ((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.size(); j++) {
                                        		((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected = !((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected;
                                			}
                                		}
                                	}
                        		}
                        	}
                		}
        				//END
        				off = beginOff;
        			}
        		}
        		off++;
        		if(((IDList)fileArray.get(0)).containers.get(0) instanceof SaveList) {
        			for(int j = 0; j < ((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.size(); j++) {
        				if(mouseX > 10 && mouseX < 390) {
                			if(mouseY > 30 && mouseY > 40 + 20*off + offset && mouseY < 50 + 20*off + offset) {
                				((Save)((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j)).selected = !((Save)((SaveList)((IDList)fileArray.get(0)).containers.get(i)).saves.get(j)).selected;
                			}
                		}
                		off++;
        			}
        		}
        		else {
        			for(int k = 0; k < ((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.size(); k++) {
        				//BEGIN
        				if(((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(0) instanceof SaveList) {
                			for(int j = 0; j < ((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.size(); j++) {
                        		//((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)).selected = !((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)).selected;
                			}
                		}
                		else {
                			for(int l = 0; l < ((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.size(); l++) {
                        		if(((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(0) instanceof SaveList) {
                        			for(int j = 0; j < ((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.size(); j++) {
                                		((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected = !((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected;
                        			}
                        		}
                        	}
                		}
        				//END
                		off++;
                		if(((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(0) instanceof SaveList) {
                			for(int j = 0; j < ((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.size(); j++) {
                				if(mouseX > 10 && mouseX < 390) {
                        			if(mouseY > 30 && mouseY > 40 + 20*off + offset && mouseY < 50 + 20*off + offset) {
                        				((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)).selected = !((Save)((SaveList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).saves.get(j)).selected;
                        			}
                        		}
                        		off++;
                			}
                		}
                		else {
                			for(int l = 0; l < ((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.size(); l++) {
                				if(((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(0) instanceof SaveList) {
                        			for(int j = 0; j < ((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.size(); j++) {
                                		((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected = !((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected;
                        			}
                        		}
                        		off++;
                        		if(((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(0) instanceof SaveList) {
                        			for(int j = 0; j < ((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.size(); j++) {
                        				if(mouseX > 10 && mouseX < 390) {
                                			if(mouseY > 30 && mouseY > 40 + 20*off + offset && mouseY < 50 + 20*off + offset) {
                                				((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected = !((Save)((SaveList)((IDList)((IDList)((IDList)fileArray.get(0)).containers.get(i)).containers.get(k)).containers.get(l)).saves.get(j)).selected;
                                			}
                                		}
                                		off++;
                        			}
                        		}
                        	}
                		}
                	}
        		}
        	}
        }
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if(!teacherOrg && !gradeOrg && !dateOrg) {
			if(offset <= 0 && offset + 20*fileArray.size() + 30 >= 380) {
				offset -= notches;
			}
		}
		else {
			if(offset <= 0) {
				offset -= notches;
			}
		}
	}
}
