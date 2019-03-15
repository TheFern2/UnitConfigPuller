import etherip.EtherNetIP;
import etherip.types.CIPData;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

// TODO
// For multiple RCFG_TD.GB_Ratio|6.808|7.55|7.88
// Output actual gear ratio matched online need a way to parse a command to log a tag at the end
// Maybe RCFG_TD.GB_Ratio|6.808|7.55|7.88|log
// Other commands besides log|range

public class UnitConfigPuller {

    public static void main(String[] args) throws Exception {

        String version = "2.0.0.";
        String settings = "Settings.ini";
        String configuration = "ConfigurationTags.ini";
        Scanner input = new Scanner(System.in);

        Ini settingsIni = new Ini(new FileReader(settings));
        FileWriter fileWriter = new FileWriter("UnitConfigLog.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        Profile.Section settingsSection = settingsIni.get("Connection");
        String plcIP = settingsSection.get("ip");
        int plcSlot = Integer.parseInt(settingsSection.get("slot"));


        try
                (
                        EtherNetIP plc = new EtherNetIP(plcIP,
                                plcSlot);
                ) {
            plc.connectTcp();
            System.out.println();

            Ini configIni = new Ini(new FileReader(configuration));
            int comparisonCounter = 0;

            //System.out.println("Number of sections: " + configIni.size() + "\n");
            for (String sectionName : configIni.keySet()) {
                //System.out.println("[" + sectionName + "]");
                Profile.Section section = configIni.get(sectionName);
                comparisonCounter = 0;
                for (String optionKey : section.keySet()) {

                    //System.out.println("\t" + optionKey + "=" + section.get(optionKey));
                    String[] tempData = section.get(optionKey).split("\\|");

                    try {
                        CIPData tempTag = plc.readTag(tempData[0]);
                        //System.out.println(tempTag.getNumber(0).toString());
                        // TODO compare to more than 1 value here
                        // Create method to parse tags
                        if (tempTag.getNumber(0).toString().equals(tempData[1])) {
                            comparisonCounter++;
                        }

                        if (comparisonCounter == section.keySet().size()) {
                            System.out.println(sectionName);
                            printWriter.println(sectionName);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }

            printWriter.close();
            System.out.println("Hit ENTER to continue...");
            input.nextLine();

        } catch (Exception e){
            System.out.println("Can't connect to PLC, check settings.ini IP or slot" + " (" + e + ")");
        }
    }

}
