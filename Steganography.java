import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

/**
 * Created by Chelsea on 3/5/2016.
 */


public class Steganography {
    public static final int ENCODE = 0;
    public static final int DECODE = 1;
    public static final int INVALID = -1;

    public static void main(String args[]) {
        String flagString = args[0];
        String image_filepath = args[1];
        String message_filepath = args[2];
        int flag = setFlags(flagString);

        ImageManager manager = new ImageManager(image_filepath);


        System.out.println("Image filename: " + manager.getFilename());
        System.out.println("Image height and width: " + manager.getHeight() + ", " + manager.getWidth());
        System.out.println("Number of pixels in image: " + manager.getNumPixels());

        if(flag == ENCODE) {
            Scanner text_file;
            try {
                text_file = new Scanner(new File(message_filepath));
            } catch (FileNotFoundException e) {
                System.out.println("ASCII file not found.");
                return;
            }
            manager.encode(text_file);
        }
        if(flag == DECODE) {
            manager.decode(message_filepath);
        }

        return;

    }

    public static int setFlags(String flagString){
        if (flagString.equals("-E") || flagString.equals("-e"))
            return ENCODE;

        else if(flagString.equals("-D") || flagString.equals("-d"))
            return DECODE;
        else
            return INVALID;
    }



}
