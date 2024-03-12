
package com.mycompany.test;


import java.io.*;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Scanner;

public class MultiMediaSound {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException 
    {
        FileInputStream fileInputStream = null;
        Scanner sc = new Scanner (System.in);
        System.out.println("Enter the path of Audio file with out double qoutation : ");
        String The_File = sc.nextLine();
        
        try {
            
            File file = new File (The_File);
            fileInputStream = new FileInputStream(The_File);
            
            // Read the header information
            //The header of WAV is 44 Byte
            byte[] Read_Headerr= new byte[44]; 
            fileInputStream.read(Read_Headerr);
 
            
           if (Read_Headerr[0]=='R' && Read_Headerr[1]=='I' && Read_Headerr[2]=='F' && Read_Headerr[3]=='F')
           {
               
            // Display the header information
               
            System.out.println("Header information:");
            System.out.println("ChunkID : "+ new String(Read_Headerr,0,4));
            int ChunkSize = FromByteToint(Read_Headerr, 4);
            System.out.println("Chunk Size : " + ChunkSize );
            System.out.println("Format : "+ new String(Read_Headerr,8,4));
            System.out.println("SubChunk 1ID: " + new String(Read_Headerr, 12, 4));
            int SubChunk1Size = FromByteToint(Read_Headerr, 16);
            System.out.println("SubChunk1Size: " + SubChunk1Size);
            short AudioFormat = FromByteToshort(Read_Headerr, 20);
            System.out.println("Audio Formate : "+ AudioFormat );
            int x = FromByteToshort(Read_Headerr, 22);
            short Num_Of_Channel = FromByteToshort(Read_Headerr, 22);
            System.out.print("Number of Channel : "+ Num_Of_Channel);
            System.out.print("  ");
            The_Channnel(x);
            int sampleRate = FromByteToint(Read_Headerr, 24);
            System.err.println("The Sampel Rate : "+ sampleRate+ " Hz");
            int Byte_rate =  FromByteToint(Read_Headerr, 28);
            System.err.println("Byte Rate : "+ Byte_rate);
            System.out.println("BlockAlign : "+ FromByteToshort(Read_Headerr, 32));
            System.out.println("Bits Per Sampel :"+ FromByteToshort(Read_Headerr, 34) +" bit");
            System.out.println("SubChunh2ID : "+new String(Read_Headerr, 36,4));
            System.out.println("SubChunk2Size : "+ FromByteToint(Read_Headerr, 40));
           }
           // The audio is OGG
           
           else if (Read_Headerr[0]=='O' && Read_Headerr[1]=='g' && Read_Headerr[2]=='g' && Read_Headerr[3]=='S'){
               
         
            System.out.println("Capture Pattern : "+ new String(Read_Headerr,0,4));
            System.out.println("This file is OGG audio file format");
            System.out.println("Virsion :" + (Read_Headerr [4] & 0xFF));      
            System.out.println("Header Type: " + (Read_Headerr [5] & 0xFF));
            
            //The granule Position offset is 6 and lenght is 8 bytes
            long granulePosition = 0;
            for (int i = 0; i < 8; i++) {
                granulePosition |= ((long) (Read_Headerr [i + 6] & 0xFF)) << (i * 8);
            }
            System.out.println("Granule Position: " + granulePosition);
            
            System.out.println("Bitstream sireal number :" + FromByteToint(Read_Headerr, 14));
            System.out.println("Page sequence number " + FromByteToint(Read_Headerr, 18));
            System.out.println("checkSum : " + FromByteToint(Read_Headerr, 22));
            
            System.out.println("Page Segmant : "+(Read_Headerr [26]& 0xFF));
            
            //To Know what is the segmant table bytes to use it to Know Segmant table
            RandomAccessFile ff = new RandomAccessFile(file, "r");
            ff.seek(27);
            int segmentTableBytes = 0;
            int byteValue;

            do {
                byteValue = ff.readByte() & 0xFF;
                segmentTableBytes++;
            } while (byteValue == 255);
            
            System.out.println("Number of bytes in Segment Table: " + segmentTableBytes);
            
            long segmantTable = 0;
            
            for (int i = 0; i < segmentTableBytes; i++) {
                segmantTable |= ((long) (Read_Headerr [i + 27] & 0xFF)) << (i * segmentTableBytes);
            }
            
            System.out.println("Segmant Table :" + segmantTable);
           
           }
           
           else 
           {
               System.out.println("Sorry ...... This project only support WAV and OGG");
           }
           
            
        } 
          catch (FileNotFoundException exx)
        {
            System.out.println("File not found");    
        }
       
      catch (IOException ex) {
            System.out.println("Error reading the file");
        }
               
    }
    private static short FromByteToshort(byte[] bytes, int offset)
    {  
 
        return (short) ((bytes[offset + 1] & 0xFF) << 8 | (bytes[offset] & 0xFF)); 

    } 
    private static int FromByteToint(byte[] bytes, int offset) { 

        return (bytes[offset + 3] & 0xFF) << 24 | (bytes[offset + 2] & 0xFF) << 16 |(bytes[offset + 1] & 0xFF) << 8 | (bytes[offset] & 0xFF); 
    } 
    
    public static void  The_Channnel(int num){
        if (num == 1)
            System.out.println("Mono");
        else if (num == 2)
            System.out.println("Stereo");
        
    }
    
 }

