import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Chelsea on 3/5/2016.
 */
public class ImageManager {
    private BufferedImage img;
    private int height;
    private int width;
    private int numPixels;
    private String filename;
    private String filenameOut;
    private String extension;
    private int decode;
    private BufferedImage imgOut;
    //message out for decode

    public ImageManager(String filepath){
        try {
            img = ImageIO.read(new File(filepath));
        } catch (IOException e) {

        }
        filename = filepath;
        height = img.getHeight();
        width = img.getWidth();

        numPixels = height * width;
        imgOut = new BufferedImage(width, height, img.getType());
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public int getNumPixels(){
        return numPixels;
    }

    public String getFilename(){
        return filename;
    }

    public void encode(Scanner message){
        System.out.println("Image type: " + img.getType());
        makeFilename();
        File outputFile;
        outputFile = new File(filenameOut);
        try {
            outputFile.createNewFile();
        }
        catch(IOException e){
            System.out.println("Error creating output file");
        }

        message.useDelimiter("");
        //each pixel has 3 indice
        //store current pixel, current index
        int currentRow = 0;
        int currentColumn = 0;
        int currentIndex = 0;
        int tempRgb = 0;
        while(message.hasNext()){
            if(currentRow >= height)
                break;
            char currentChar = message.next().charAt(0);
            for(int i = 0; i < 8; i++){
                //for each bit, pick current pixel in image, and encode
                int bit = (currentChar >> (7 - i)) & 1;
                int rgb = img.getRGB(currentColumn, currentRow) & (0b111111111111111111111111);
                int rgbbyte = rgb >> (((2 - currentIndex) * 8)) & 0b11111111;
                if(bit == 0){
                    if(rgbbyte % 2 == 0){
                        //already even, do nothing
                    }
                    else{
                        if(rgbbyte == 255)
                            rgbbyte--;
                        else
                            rgbbyte++;
                    }

                }
                else{
                    if(rgbbyte % 2 == 1){
                        //rgb is already odd, do nothing
                    }
                    else{
                        if(rgbbyte == 0)
                            rgbbyte++;
                        else
                            rgbbyte--;
                    }
                }
                tempRgb = tempRgb | (rgbbyte << ((2 - currentIndex) * 8));

                currentIndex++;

                if(currentIndex >= 3){
                    imgOut.setRGB(currentColumn, currentRow, tempRgb);
                    tempRgb = 0;
                    currentIndex = 0;
                    currentColumn++;

                }
                if(currentColumn >= width){
                    //next row
                    currentRow++;
                    currentColumn = 0;
                }
                if(currentRow == height - 1 && currentColumn == width - 3 && currentIndex == 1){
                    System.out.println("Ran out of room! Message truncated.");

                    break;

                }
            }
        }
        //Crashes here if image less than 3 pixels wide
        for(int i = 0; i < 8; i++){
            int rgb = img.getRGB(currentColumn, currentRow) & (0b111111111111111111111111);
            int rgbbyte = rgb >> (((2 - currentIndex) * 8)) & 0b11111111; //check this
            if(rgbbyte % 2 == 0){
                //already even, do nothing
            }
            else{
                if(rgbbyte == 255)
                    rgbbyte--;
                else
                    rgbbyte++;
            }
            tempRgb = tempRgb | (rgbbyte << ((2 - currentIndex) * 8));
            currentIndex++;
            if(currentIndex >= 3){
                //write rbg value
                //reset working rbg value to zero
                imgOut.setRGB(currentColumn, currentRow, tempRgb);
                tempRgb = 0;
                currentIndex = 0;
                currentColumn++;

            }
            if(currentColumn >= width){
                //next row
                currentRow++; //check again for width
                currentColumn = 0;
            }

        }


        while(currentRow < height-1){
            if(currentIndex != 0){
                //message finished on 1 or 2, need to write the rest of it
                //grab rgb of bit 1 and 2 if finished on 1, bit 2 if finished on 2
                if(currentIndex == 1){
                    tempRgb = tempRgb | (img.getRGB(currentColumn, currentRow) & 0b1111111111111111);
                    imgOut.setRGB(currentColumn, currentRow, tempRgb);
                    currentColumn++;
                }
                else if(currentIndex == 2){
                    tempRgb = tempRgb | (img.getRGB(currentColumn, currentRow) & 0b11111111);
                    imgOut.setRGB(currentColumn, currentRow, tempRgb);
                    currentColumn++;
                }
                //System.out.println("Current index: " + currentIndex);
                currentIndex = 0;
            }
            //finished up rest of loose bits, now write the rest
            if(currentColumn >= width){
                //next row
                currentRow++; //check again for width
                currentColumn = 0;
            }
            imgOut.setRGB(currentColumn, currentRow, img.getRGB(currentColumn, currentRow));
            currentColumn++;
        }
        try {
            System.out.println("Writing output file");
            ImageIO.write(imgOut, extension.substring(1), outputFile);
        }
        catch(IOException e){
            System.out.println("Error creating output file.");
        }

    }


    public void decode(String txtOut){
        FileOutputStream output;
        try {
            output = new FileOutputStream(new File(txtOut));
        }
        catch(FileNotFoundException e){
            System.out.println("Error creating output file.");
            return;
        }
        int charByte = 0b0;
        int counter = 0;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int rgb = img.getRGB(j,i) & 0b111111111111111111111111;
                for(int k = 0; k < 3; k++){
                    //grab rgb at index
                    int rgbbyte = rgb >> (((2 - k) * 8)) & 0b11111111;
                    //check if even or odd
                    int bit = rgbbyte % 2;
                    //update charbyte depending
                    charByte = charByte | (bit << (8 - (counter + 1)));
                    //increment counter
                    counter++;
                    //if counter == 8 then write char to text file
                    if(counter == 8){
                        counter = 0;
                        if(charByte == 0){
                            return;
                        }
                        try {
                            output.write((char) charByte);
                        }
                        catch(IOException e){
                            System.out.println("Error writing to output file.");
                        }
                        charByte = 0;
                    }
                }
            }
        }

    }

    private void makeFilename(){
        int extensionIndex = 0;
        for(int i = 0; i < filename.length(); i++){
            if(filename.charAt(i) == '.')
                break;
            extensionIndex++;
        }

        filenameOut = filename.substring(0, extensionIndex) + "-steg" + filename.substring(extensionIndex);
        extension = filename.substring(extensionIndex);
        System.out.println(filenameOut);
    }
}

