package pick.part.FileIO;

import java.io.BufferedReader;
import java.io.FileReader;

import pick.part.src.FileViewer;
import pick.part.*;

public class Save extends Filum {
	public String url = "";
	public String name = "";
	public boolean selected = false;
	
	private int happiness = 0;
    private int fossilFyools = 0;
    private int waters = 0;
    private int science = 0;
    private int population = 2;
    private double munnies = 150;
    private double energies = 0;
    private double pollution = 0;
    private int foodings = 0;
    double taxes = 0;
    int ffreq = 0;
    int tutorialIndex = 0;
    
    public String bLog = "";
    public String rLog = "";
    public String wLog = "";
    
    public String vALog = "";
    public String hLog = "";
    
    int numCoal = 0;
    int numWat = 0;
    public String cLog = "";
    public String aLog = "";
    public long timeLine = 0;
    public String[] aEntries;
    public String[] bEntries;
    public String[] cEntries;
    public String[] rEntries;
    public String[] wEntries;
    public String[] hEntries;
    
    float[] aTimeRats;
    float[] bTimeRats;
    float[] cTimeRats;
    float[] rTimeRats;
    float[] wTimeRats;
    
    public int maxa = 0;
    public int maxc = 0;
    public int maxh = 0;
    
    public String teacher = "";
    public String grade = "";
    public String date = "";
    public String attempt = "";
    public String playerName = "";
	
	public void load() {
		playerName = FileViewer.parseName(name);
		grade = FileViewer.parseGrade(name);
    	try {
    		BufferedReader in = new BufferedReader(new FileReader(url));
    		grade = FileViewer.parseGrade(name);
    		taxes = Double.parseDouble(in.readLine());
    		ffreq = Integer.parseInt(in.readLine());
    		happiness = Integer.parseInt(in.readLine());
    		fossilFyools = Integer.parseInt(in.readLine());
    		waters = Integer.parseInt(in.readLine());
    		science = Integer.parseInt(in.readLine());
    		population = Integer.parseInt(in.readLine());
    		munnies = Double.parseDouble(in.readLine());
    		pollution = Double.parseDouble(in.readLine());
    		foodings = Integer.parseInt(in.readLine());
    		tutorialIndex = Integer.parseInt(in.readLine());
    		int tot = Integer.parseInt(in.readLine());
    		//buildings.clear();
    		for(int i = 0; i < tot; i++) {
    			String intermediate = in.readLine();
    			/*String[] nums = seperate(intermediate, 'x');
    			if(Integer.parseInt(nums[0]) == 0) {
    				buildings.add(new Tree());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 1) {
    				buildings.add(new House());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 2) {
    				buildings.add(new Mine());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 3) {
    				buildings.add(new PowerPlant());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 4) {
    				buildings.add(new ScienceFacility());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 5) {
    				buildings.add(new SolarPlant());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 6) {
    				buildings.add(new RecyclePlant());
    				recycleCount += 1;
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 7) {
    				buildings.add(new Nuclear());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 8) {
    				buildings.add(new Landfill());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}
    			else if(Integer.parseInt(nums[0]) == 9) {
    				buildings.add(new WaterCleaner());
    				buildings.get(buildings.size() - 1).x = Integer.parseInt(nums[1]);
    				buildings.get(buildings.size() - 1).y = Integer.parseInt(nums[2]);
    			}*/
    		}
    		int to = Integer.parseInt(in.readLine());
    		//turtles.clear();
    		for(int i = 0; i < to; i++) {
    			String intermediate = in.readLine();
    			String[] nums = intermediate.split("x");
    			//turtles.add(new MiningTurtle(Integer.parseInt(nums[0]),Integer.parseInt(nums[1])));
    			//turtles.get(turtles.size() - 1).rightIndex = Integer.parseInt(nums[2]);
    			//turtles.get(turtles.size() - 1).leftIndex = Integer.parseInt(nums[3]);
    		}
    		int toto = Integer.parseInt(in.readLine());
    		//watTurt.clear();
    		for(int i = 0; i < toto; i++) {
    			String intermediate = in.readLine();
    			String[] nums = intermediate.split("x");
    			//watTurt.add(new WaterDirtyTurtle(Integer.parseInt(nums[0]),Integer.parseInt(nums[1])));
    			//watTurt.get(watTurt.size() - 1).rightIndex = Integer.parseInt(nums[2]);
    			//watTurt.get(watTurt.size() - 1).leftIndex = Integer.parseInt(nums[3]);
    		}
    		int tot0 = Integer.parseInt(in.readLine());
    		//nucTurt.clear();
    		for(int i = 0; i < tot0; i++) {
    			String intermediate = in.readLine();
    			String[] nums = intermediate.split("x");
    			//nucTurt.add(new WaterDirtyTurtle(Integer.parseInt(nums[0]),Integer.parseInt(nums[1])));
    			//nucTurt.get(nucTurt.size() - 1).rightIndex = Integer.parseInt(nums[2]);
    			//nucTurt.get(nucTurt.size() - 1).leftIndex = Integer.parseInt(nums[3]);
    		}
    		int t = Integer.parseInt(in.readLine());
    		//cleanTurt.clear();
    		for(int i = 0; i < t; i++) {
    			String intermediate = in.readLine();
    			String[] nums = intermediate.split("x");
    			//cleanTurt.add(new WaterCleanTurtle(Integer.parseInt(nums[0]),Integer.parseInt(nums[1])));
    			//cleanTurt.get(cleanTurt.size() - 1).rightIndex = Integer.parseInt(nums[2]);
    			//cleanTurt.get(cleanTurt.size() - 1).leftIndex = Integer.parseInt(nums[3]);
    		}
    		int t0 = Integer.parseInt(in.readLine());
    		//drinkingTurtles.clear();
    		for(int i = 0; i < t0; i++) {
    			String intermediate = in.readLine();
    			String[] nums = intermediate.split("x");
    			//drinkingTurtles.add(new WaterTurtle(Integer.parseInt(nums[0]),Integer.parseInt(nums[1])));
    			//drinkingTurtles.get(cleanTurt.size() - 1).rightIndex = Integer.parseInt(nums[2]);
    			//drinkingTurtles.get(cleanTurt.size() - 1).leftIndex = Integer.parseInt(nums[3]);
    		}
    		for(int i = 0; i < 175; i++) {
    			String intermediate = in.readLine();
    			String[] nums = intermediate.split("x");
    			for(int j = 0; j < nums.length; j++) {
    				/*if(Integer.parseInt(nums[j]) == 0) {
    					chunks.get(0).blocks[i][j] = new SandBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 1) {
    					chunks.get(0).blocks[i][j] = new PermaJordBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 2) {
    					chunks.get(0).blocks[i][j] = new PermaGrassBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 3) {
    					chunks.get(0).blocks[i][j] = new LimestoneBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 4) {
    					chunks.get(0).blocks[i][j] = new DirtBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 5) {
    					chunks.get(0).blocks[i][j] = new StoneBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 6) {
    					chunks.get(0).blocks[i][j] = new AquiferBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 7) {
    					chunks.get(0).blocks[i][j] = new DirtyWaterBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 8) {
    					chunks.get(0).blocks[i][j] = new CoalBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 9) {
    					chunks.get(0).blocks[i][j] = new DiscoFFBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 10) {
    					chunks.get(0).blocks[i][j] = new DarkStoneBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 11) {
    					chunks.get(0).blocks[i][j] = new DarkDirtBlock();
    				}
    				if(Integer.parseInt(nums[j]) == 12) {
    					chunks.get(0).blocks[i][j] = null;
    				}*/
    			}
    		}
    		//inGames.clear();
    		//munnythings.clear();
    		//faces.clear();
    		//electrics.clear();
    		//co2Thingies.clear();
    		//calculatePerCapitas();
    		bLog = in.readLine();
    		rLog = in.readLine();
    		wLog = in.readLine();
    		cLog = in.readLine();
    		aLog = in.readLine();
    		timeLine = Long.parseLong(in.readLine());
    		//vALog = in.readLine();
    		hLog = in.readLine();
    		//System.out.println(bLog);
    		//System.out.println(rLog);
    		//System.out.println(wLog);
    		//System.out.println(cLog);
    		//System.out.println(aLog);
    		//System.out.println(timeLine);
    		in.close();
    		aEntries = aLog.split("x");
    		bEntries = bLog.split("x");
    		cEntries = cLog.split("x");
    		rEntries = rLog.split("x");
    		wEntries = wLog.split("x");
    		//hEntries = hLog.split("x");
    		for(int i = 0; i < aEntries.length; i++) {
    			String[] poop = aEntries[i].split(":");
    			//System.out.println(poop[0]);
    			if(Integer.parseInt(poop[0]) > maxa) {
    				maxa = Integer.parseInt(poop[0]);
    			}
    		}
    		for(int i = 0; i < cEntries.length; i++) {
    			String[] poop = cEntries[i].split(":");
    			if(Integer.parseInt(poop[0]) > maxc) {
    				maxc = Integer.parseInt(poop[0]);
    			}
    		}
    		/*for(int i = 0; i < hEntries.length; i++) {
    			String[] poop = hEntries[i].split(":");
    			if(Integer.parseInt(poop[0]) > maxh) {
    				maxh = Integer.parseInt(poop[0]);
    			}
    		}*/
    	} catch(Exception e) {System.out.println("Something went wrong");}
    }
	
	public Save(String address, String n) {
		url = address;
		name = n;
	}
}
