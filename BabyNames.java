import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
/**
 * Write a description of BabyNames here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BabyNames {
    public void totalBirths (FileResource fr){
        int totalBirths = 0;
        int totalGirls = 0;
        int totalBoys = 0;
        int girlsName = 0;
        int boysName = 0;
        int totalNames = 0;
        for (CSVRecord rec : fr.getCSVParser(false)){
            int numBorn = Integer.parseInt(rec.get(2));
            totalBirths += numBorn;
            totalNames += 1;
            if (rec.get(1).equals("F")) {
                girlsName = girlsName + 1;
                totalGirls += numBorn;
            } else {
                boysName = boysName + 1;
                totalBoys += numBorn;
            }
        }
        System.out.println("Total number of Births: " + totalBirths);
        System.out.println("Total number of Girls: " + totalGirls); 
        System.out.println("Total number of Boys: " + totalBoys);
        System.out.println("Total number of Names: " + totalNames);
        System.out.println("Total number of Girls Name: " + girlsName); 
        System.out.println("Total number of Boys Name: " + boysName);       
    }

    public void testTotalBirths (){
        FileResource fr = new FileResource();
        totalBirths(fr);
    }

    public int getRank (int year, String name, String gender) {
        int maleRank = 0;
        int femaleRank = 0;
        String file = "us_babynames_by_year/yob"+String.valueOf(year)+".csv";
        FileResource fr = new FileResource(file);
        for (CSVRecord rec : fr.getCSVParser(false)){
            if (rec.get(1).equals("M") && gender.equals("M")) {
                maleRank = maleRank + 1;
                if (rec.get(0).toUpperCase().equals(name.toUpperCase())) {
                    return maleRank;
                }
            } else if (rec.get(1).equals("F") && gender.equals("F")) {
                femaleRank = femaleRank + 1;
                if (rec.get(0).toUpperCase().equals(name.toUpperCase())){
                    return femaleRank;
                }
            }
        }
        return -1;
    }

    public void testGetRank () {
        System.out.println("Rank of Emily is " + getRank(1960, "Emily", "F"));
    }
    
    public String getName(int year, int rank, String gender) {
        int maleRank = 0;
        int femaleRank = 0;
        String file = "us_babynames_by_year/yob"+String.valueOf(year)+".csv";
        FileResource fr = new FileResource(file);
        for(CSVRecord rec : fr.getCSVParser(false)){
            if (rec.get(1).equals("M") && gender.equals("M")) {
                maleRank++;
                if (maleRank == rank){
                    return rec.get(0);
                }
            } else if (rec.get(1).equals("F") && gender.equals("F")) {
                femaleRank++;
                if (femaleRank == rank){
                    return rec.get(0);
                }
            }
        }
        return "NO NAME";
    }
    
    public void testGetName() {
        System.out.println("Name at that rank is " + getName(1980, 350, "F"));
    }
    
    public String whatIsNameInYear(String name, int year, int newYear, String gender){
        int yourRankNow = getRank(year, name, gender);
        String yourNameAtNewYear = getName(newYear, yourRankNow, gender);
        String output = name + " born in " + String.valueOf(year) + " would be " + yourNameAtNewYear + " if she was born in " + String.valueOf(newYear);
        return output;
    }
    
    public void testWhatIsNameInYear(){
        System.out.println(whatIsNameInYear("Susan", 1972, 2014, "F"));
    }
    
    public int yearOfHighestRank(String name, String gender) {
        int largestRank = 1000000000;
        int largestYear = -1;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            String filename = f.getName();
            int year = Integer.parseInt(filename.replaceAll("\\D+", ""));
            int currentRank = getRank(year, name, gender);
            if (largestRank == 1000000000 && currentRank != -1) {
                largestRank = currentRank;  
                largestYear = year;             
            } else if(currentRank == -1) {
                largestYear = -1;
            } else {
                if (currentRank < largestRank) {
                    largestRank = currentRank;
                    largestYear = year;
                }
            }
        }
        return largestYear;         
    } 
	
    
    public void testYearOfHighestRank() {
        System.out.println("Saman was ranked highest in " + yearOfHighestRank("Saman", "M"));
    }
    
    public double getAverageRank(String name, String gender) {
        double totalRank = 0;
        int count = 0;
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            count++;
            String filename = f.getName();
            int year = Integer.parseInt(filename.replaceAll("\\D+", ""));
            int currentRank = getRank(year, name, gender);
            if (currentRank == -1){
                currentRank = 0;
                totalRank += currentRank;
            } else {
                totalRank += currentRank;
            }
        }
        if (totalRank == 0){
            return -1;
        } else {
            return totalRank/count;
		}
    }
    
    public void testGetAverageRank() {
        System.out.println("Mason has an average rank of " + getAverageRank("Mason", "M"));
    }
	
	public int getTotalBirthsRankedHigher(int year, String name, String gender) {
		int totalMaleBirths = 0;
		int totalFemaleBirths = 0;
		String file = "us_babynames_by_year/yob"+String.valueOf(year)+".csv";
        FileResource fr = new FileResource(file);
		for(CSVRecord rec : fr.getCSVParser(false)){
			if (rec.get(1).equals("M") && gender.equals("M")){
				if (rec.get(0).equals(name)) {
					return totalMaleBirths;
				} else {
					int numBorn = Integer.parseInt(rec.get(2));
					totalMaleBirths += numBorn;
				}
			} else if(rec.get(1).equals("F") && gender.equals("F")){
				if (rec.get(0).equals(name)) {
					return totalFemaleBirths;
				} else {
					int numBorn = Integer.parseInt(rec.get(2));
					totalFemaleBirths += numBorn;
				}
			}
		}
		return 0;
	}
	
	public void testGetTotalBirthsRankedHigher() {
		System.out.println("Total Births Ranked Higher is " + getTotalBirthsRankedHigher(2012, "Isabella", "F"));
	} 
}
