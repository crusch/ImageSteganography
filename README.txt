UTEID: crr2494;
FIRSTNAME: Chelsea;
LASTNAME: Rusch;
CSACCOUNT: crusch;
EMAIL: chelsearusch@utexas.edu;

[Program 3]
[Description]
There are 2 Java files - Steganography and ImageManager. Steganography handles all the main function level things, while ImageManager handles 
the decoding, encoding, and file output. The program can be compiled with javac *.java and run with java Steganography [flag] [image] [textfile]

[Finish]
I finished most requirements, but I only tested png files. The program does not work for images that are less than 3 pixels wide and
may have some bugs around the image size similar to message size threshold. It also cannot handle malformed command line arguments.

[Questions&Answers]

[Question 1]
Comparing your original and modified images carefully, can you detect *any* difference visually (that is, in the appearance of the image)?

[Answer 1]
No.

   
[Question 2]
Can you think of other ways you might hide the message in image files (or in other types of files)?

[Answer 2]
Using metadata, or characters/whitespace in the file. (vowel = 0, consonant = 1, etc)


[Question 3]
Can you invent ways to increase the bandwidth of the channel?

[Answer 3]
Since there are 3 bytes per each RGB value, you could use something like the least significant 3 bits of each field in the RBG value, but 
that might change the result's appearance more drastically.


[Question 4]
Suppose you were tasked to build an "image firewall" that would block images containing hidden messages. Could you do it? How might you approach this problem?

[Answer 4]
You could block images that can be decoded in a way that maps to common ASCII characters, even if you don't implement a way to interpret them.


[Question 5]
Does this fit our definition of a covert channel? Explain your answer.

[Answer 5]
Yes, because the image file format is being used to transmit information it isn't intended to transmit, in this case a text file.