package picture;

import java.util.Arrays;

public class PictureProcessor {

  public static void main(String[] args) {
    // TODO: Implement this.
    System.out.println("Hellog");
    System.err.println("TODO: Implement main HELLO");

    String command = args[0];

    switch (command) {
      case "invert":
        Picture inPic = new Picture(args[1]);
        inPic.invert();
        inPic.saveAs(args[2]);
        break;
      case "grayscale":
        Picture grPic = new Picture(args[1]);
        grPic.grayscale();
        grPic.saveAs(args[2]);
        break;
      case "rotate":
        Picture rotPic = new Picture(args[2]);
        rotPic.rotate(args[1]).saveAs(args[3]);
        break;
      case "flip":
        Picture fPic = new Picture(args[2]);
        switch (args[1]) {
          case "H": fPic.flipHorizontal().saveAs(args[3]);
          case "F": fPic.flipVerticle().saveAs(args[3]);
        }
        break;
      case "blur":
        Picture blrPic = new Picture(args[1]);
        blrPic.blur().saveAs(args[2]);
        break;
      case "blend":
        Picture blnPic = new Picture(args[1]);
        String[] inputs = Arrays.copyOfRange(args, 1, args.length -1);
        Picture[] pics = Arrays.stream(inputs).map(Picture::new).toList().toArray(new Picture [args.length-2]);
        blnPic.blend(pics).saveAs(args[args.length-1]);
      }
    }
}

