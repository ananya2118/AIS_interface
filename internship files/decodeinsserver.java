

// import java.net.*;
// import java.io.*;

// public class decodeinsserver {
//     public static void main(String args[])throws Exception
//     {
//         ServerSocket sersock=new ServerSocket(3456);
//         Socket socke=sersock.accept();
//         InputStreamReader istream=new InputStreamReader(socke.getInputStream());
//         OutputStreamWriter ostream=new OutputStreamWriter(socke.getOutputStream());
//         BufferedReader br=new BufferedReader(istream);
//         BufferedWriter bw=new BufferedWriter(ostream);
//         // Scanner sc=new Scanner(System.in);
//         try{

//             String ais_message=br.readLine();
//             System.out.println("client : "+ais_message);


//     }
//     catch (Exception e) {
//         e.printStackTrace();
//     }
//     finally{
//         bw.close();
//         br.close();
//         socke.close();
//         sersock.close();
//         // sc.close();
//     }
// }
// }






import java.net.*;
import java.io.*;

public class decodeinsserver 
{

    static void typeofpacket(String typeofpacket)
    {
        if(typeofpacket.equals("!AIVDM")) 
        System.out.println("its is an AIVDM packet");
        if(typeofpacket.equals("!AIVDO")) 
        System.out.println("its is an AIVDO packet");
    }
    static void messagecount(String i)
    {
        System.out.println("the total number of messages is : "+Integer.parseInt(i));
    }
    static void messagenumber(String n)
    {
        System.out.println("the number of the message is : "+Integer.parseInt(n));
    }
    static void messageid(String messadeid)
    {
        try{
        int messageidinteger=Integer.parseInt(messadeid);
        System.out.println("the sequential message id is : "+messageidinteger);
        }
        catch(Exception e)
        {

        }
    }
    static void channelcode(String channelcode)
    {
        System.out.println("the channel code is : "+channelcode);
    }
    

    static void calcpayload(String payload)
    {
        //covert payload str to char array
        /*convert character to ascii
        To recover the six bits, subtract 48 from the ASCII character value; if the result is greater than 40 subtract 8.begin with "0" (64) and end with "W" (87); however, the intermediate range "X" (88) to "\_" (95) is not used.
        length of payload = 28 char, bits =168 bits*/
        char payloadChar[] = payload.toCharArray();
        if(payloadChar.length!=28)
        {
            System.out.println("Message is not of type 1,2 or 3.Try other message");
            System.exit(0);
        }
        try
        {
            int payloadAscii[] = new int[payloadChar.length];
            int payloadDec[] = new int[payloadChar.length];
            String payloadBin="";
            for(int i=0;i<payloadChar.length;i++)
            {
                payloadAscii[i] =(int)payloadChar[i];
                payloadDec[i] = payloadAscii[i]-48;
                if (payloadDec[i]>40)
                payloadDec[i]=payloadAscii[i]-48-8;
            }
            //decimal to 6 bit binary  
            String bin6;
            for(int i=0;i<payloadDec.length;i++)
            { 
                String bin = Integer.toBinaryString(payloadDec[i]);
                char paddingCharacter = '0'; 
                if (bin.length() <= 6) {
                int paddingLength = 6 - bin.length();
                String padding = new String(new char[paddingLength]).replace('\0', paddingCharacter);
                bin6 = padding + bin;             
                payloadBin=payloadBin+bin6;
                
            }
            }
            String messageidbin=payloadBin.substring(0, 6);
            int messageiddec=Integer.parseInt(messageidbin,2);
            System.out.println("the message type is : "+messageiddec);

            String repeatindicatorbin=payloadBin.substring(7, 8);
            int repeatindicatordec=Integer.parseInt(repeatindicatorbin,2);
            System.out.print("the repeat indicator is : ");
            if(repeatindicatordec==0)
            System.out.println("default");
            if(repeatindicatordec==3)
            System.out.println("do not repeat anymore");
            
            String mmsibin=payloadBin.substring(9, 38);
            int mmsidec=Integer.parseInt(mmsibin,2);
            System.out.println("the mmsi number is : "+mmsidec);

            String navigationstatusbin=payloadBin.substring(39, 42);
            int navigationstatusdec=Integer.parseInt(navigationstatusbin,2);
            System.out.print("the navigational status is : "+navigationstatusdec +"  ");
            switch(navigationstatusdec)
            {
                case 0:
                System.out.println("under way using engine");
                break;

                case 1:
                System.out.println("at anchor");
                break;

                case 2:
                System.out.println("not under command");
                break;

                case 3:
                System.out.println("restricted manoeuvrability");
                break;

                case 4:
                System.out.println("constrained by her draught");
                break;

                case 5:
                System.out.println("moored");
                break;

                case 6:
                System.out.println("aground");
                break;

                case 7:
                System.out.println(" engaged in fishing");
                break;

                case 8:
                System.out.println(" under way sailing");
                break;

                case 9:
                System.out.println(" engaged in fishing");
                break;

                case 10:
                System.out.println(" reserved for future amendment of navigational status for ships carrying DG, HS, or MP, or IMO hazard or pollutant category C (HSC)");
                break;

                case 11:
                case 12:
                case 13:
                case 14:
                System.out.println(" reserved for future use");
                break;

                default:
                System.out.println(" not defined");
                break;
            }

            // String rate_of_turn_bin=payloadBin.substring(43, 50);
            // int rotdec=Integer.parseInt(rate_of_turn_bin,2);
            // if(rate_of_turn_bin.charAt(0)=='1')
            // {
            //     rotdec-=8;
            // }
            // float rate_of_turn_float=(float)rotdec;
            // String suffix="3";
            // int decimalPlaces = Integer.parseInt(suffix);
            // rate_of_turn_float /= Math.pow(10, decimalPlaces);
            // System.out.println("the rate of turn is : "+rate_of_turn_float);

            String speed_over_ground_bin=payloadBin.substring(51, 60);
            int speed_over_ground_dec=Integer.parseInt(speed_over_ground_bin,2);
            System.out.println("the speed over ground is : "+speed_over_ground_dec);

            char position_accuracy_bin=payloadBin.charAt(60);
            System.out.print("the position accuracy is : ");
            if(position_accuracy_bin=='0')
            System.out.println("an unaugmented GNSS fix with accuracy > 10m");
            if(position_accuracy_bin=='1')
            System.out.println("DGPS-quality fix with an accuracy of < 10ms");

            //long 62-89
            //lat 90-116

            String course_over_ground_bin=payloadBin.substring(117, 128);
            int course_over_ground_dec=Integer.parseInt(course_over_ground_bin,2);
            System.out.println("the course over ground is : "+(course_over_ground_dec/10));

            String true_heading_bin=payloadBin.substring(129, 137);
            int true_heading_dec=Integer.parseInt(true_heading_bin,2);
            System.out.println("the true heading is : "+true_heading_dec);

            String time_stamp_bin=payloadBin.substring(138, 143);
            int time_stamp_dec=Integer.parseInt(time_stamp_bin,2);
            System.out.println("the time stamp is : "+time_stamp_dec);

            String reserved_for_regional_applications_bin=payloadBin.substring(144, 147);
            int reserved_for_regional_applications_dec=Integer.parseInt(reserved_for_regional_applications_bin,2);
            System.out.println("reserved for regional is : "+reserved_for_regional_applications_dec);

            char spare=payloadBin.charAt(147);
            spare='0';
            System.out.println("the spare is : "+spare);

            char RAIM_flag_bin=payloadBin.charAt(148);
            System.out.print("the RAIM flag is : "+RAIM_flag_bin +" ");
            if(RAIM_flag_bin=='0')
            System.out.println("RAIM flag not in use");
            if(RAIM_flag_bin=='1')
            System.out.println("RAIM flag in use");

            String communication_status_bin=payloadBin.substring(149, 168);
            int communication_status_dec=Integer.parseInt(communication_status_bin,2);
            System.out.println("reserved for regional is : "+communication_status_dec);

        } 
        catch (Exception e)
        {
          System.out.println(e);
        }
    }

    public static void main(String args[])throws Exception
    {
        ServerSocket sersock=new ServerSocket(3456);
        Socket socke=sersock.accept();
        InputStreamReader istream=new InputStreamReader(socke.getInputStream());
        OutputStreamWriter ostream=new OutputStreamWriter(socke.getOutputStream());
        BufferedReader br=new BufferedReader(istream);
        BufferedWriter bw=new BufferedWriter(ostream);
        try{

            String ais_message=br.readLine();
            System.out.println("AIS message : "+ais_message);
            String[] splitstr=ais_message.split(",");
            for(int i=0;i<splitstr.length;i++)
            System.out.println(splitstr[i]);
            typeofpacket(splitstr[0]);
            messagecount(splitstr[1]);
            messagenumber(splitstr[2]);
            messageid(splitstr[3]);
            channelcode(splitstr[4]);
            calcpayload(splitstr[5]);
        }
        catch (Exception e) 
        {
        e.printStackTrace();
        }
        finally
        {
        bw.close();
        br.close();
        socke.close();
        sersock.close();
        }
    }
}