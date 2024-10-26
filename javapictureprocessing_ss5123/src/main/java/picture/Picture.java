package picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class that encapsulates and provides a simplified interface for manipulating an image. The
 * internal representation of the image is based on the RGB direct colour model.
 */
public class Picture {

  /**
   * The internal image representation of this picture.
   */
  private final BufferedImage image;

  /**
   * Construct a new (blank) Picture object with the specified width and height.
   */
  public Picture(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * Construct a new Picture from the image data in the specified file.
   */
  public Picture(String filepath) {
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test if the specified point lies within the boundaries of this picture.
   *
   * @param x the x co-ordinate of the point
   * @param y the y co-ordinate of the point
   * @return <tt>true</tt> if the point lies within the boundaries of the picture, <tt>false</tt>
   * otherwise.
   */
  public boolean contains(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  /**
   * Returns true if this Picture is graphically identical to the other one.
   *
   * @param other The other picture to compare to.
   * @return true iff this Picture is graphically identical to other.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof Picture)) {
      return false;
    }

    Picture otherPic = (Picture) other;

    if (image == null || otherPic.image == null) {
      return image == otherPic.image;
    }
    if (image.getWidth() != otherPic.image.getWidth()
        || image.getHeight() != otherPic.image.getHeight()) {
      return false;
    }

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getRGB(i, j) != otherPic.image.getRGB(i, j)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Return the height of the <tt>Picture</tt>.
   *
   * @return the height of this <tt>Picture</tt>.
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Return the colour components (red, green, then blue) of the pixel-value located at (x,y).
   *
   * @param x x-coordinate of the pixel value to return
   * @param y y-coordinate of the pixel value to return
   * @return the RGB components of the pixel-value located at (x,y).
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public Color getPixel(int x, int y) {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

  /**
   * Return the width of the <tt>Picture</tt>.
   *
   * @return the width of this <tt>Picture</tt>.
   */
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int hashCode() {
    if (image == null) {
      return -1;
    }
    int hashCode = 0;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        hashCode = 31 * hashCode + image.getRGB(i, j);
      }
    }
    return hashCode;
  }

  public void saveAs(String filepath) {
    try {
      ImageIO.write(image, "png", new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Update the pixel-value at the specified location.
   *
   * @param x   the x-coordinate of the pixel to be updated
   * @param y   the y-coordinate of the pixel to be updated
   * @param rgb the RGB components of the updated pixel-value
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *                                        the boundaries of this picture.
   */
  public void setPixel(int x, int y, Color rgb) {

    image.setRGB(
        x,
        y,
        0xff000000
            | (((0xff & rgb.getRed()) << 16)
            | ((0xff & rgb.getGreen()) << 8)
            | (0xff & rgb.getBlue())));
  }

  /**
   * Returns a String representation of the RGB components of the picture.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        sb.append("(");
        sb.append(rgb.getRed());
        sb.append(",");
        sb.append(rgb.getGreen());
        sb.append(",");
        sb.append(rgb.getBlue());
        sb.append(")");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  public void invert() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getWidth(); y++) {
        Color col = getPixel(x, y);
        setPixel(x, y, new Color(255 - col.getRed(), 255 - col.getGreen(), 255 - col.getBlue() ));
      }
    }
  }

  public void grayscale() {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getWidth(); y++) {
        Color col = getPixel(x, y);
        int colAvg = (col.getGreen() + col.getBlue() + col.getRed())/3;
        setPixel(x, y, new Color(colAvg, colAvg, colAvg ));
      }
    }
  }

  public Picture rotate(String angle) {
    int w = getWidth();
    int h = getHeight();
    switch (angle) {
      case "90":
        return transHelper(h, w, ((x,y) -> h-1-y), (x,y) -> y);
      case "180":
        return transHelper(w, h, ((x,y) -> w-1-x), ((x,y) -> h-1-y));
      case "270":
        return transHelper(h, w, ((x,y) -> x), ((x,y) -> w-1-x));
      default:
        throw new IllegalArgumentException();
    }
  }

  public Picture flipHorizontal() {
    int w = getWidth();
    int h = getHeight();
    return transHelper(w, h, (x, y) -> (w-1-x), (x, y) -> (y));
  }

  public Picture flipVerticle() {
    int w = getWidth();
    int h = getHeight();
    return transHelper(w, h, (x, y) -> (x), (x, y) -> (h-1-y));
  }


  public Picture transHelper(int n, int m, BinaryOperator<Integer> n1, BinaryOperator<Integer> m1){
    Picture newPic = new Picture(n, m);
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        Color col = getPixel(x,y);
        int nX = n1.apply(x,y);
        int nY = m1.apply(x,y);
        Color nRGB = new Color(col.getRed(), col.getGreen(), col.getBlue()));
        newPic.setPixel(nX, nY, nRGB);
      }
    }
    return newPic;
  }

  public Picture blend(Picture[] pics) {
    Picture minHWFrame = getMinWH(pics);
    int w = minHWFrame.getWidth();
    int h = minHWFrame.getHeight();
    Picture newPic = new Picture(w, h);
    int len = pics.length;
    for (int x = 0; x < w; w++) {
      for (int y = 0; y < h; y++){
        int r = 0;
        int g = 0;
        int b = 0;
        for (Picture pic: pics) {
          Color pix = pic.getPixel(x, y);
          r += pix.getRed();
          g += pix.getGreen();
          b += pix.getBlue();
        }
        newPic.setPixel(x, y, new Color(r/len, g/len, b/len));
      }
    }
    return newPic;
  }

  public Picture getMinWH(Picture[] pics){
    Optional<Picture> result = Arrays.stream(pics)
            .reduce((pic1, pic2) -> new Picture(
                    Math.min(pic1.getWidth(), pic2.getWidth()),
                    Math.min(pic1.getHeight(), pic2.getHeight())
            ));
    if (result.isPresent()){
      return result.get();
    } else {
      throw new IllegalArgumentException();
    }
  }
  public Picture blur(){
    saveAs("images/blurPlaceHolder.png");
    Picture copyPic = new Picture("images/blurPlaceHolder.png");
    int w = copyPic.getWidth();
    int h = copyPic.getHeight();
    for (int x = 1; x < w-1; x++) {
      for (int y = 1; y < h-1; y++) {
        copyPic.avgPixs(x,y);
      }
    }
    return copyPic;
  }

  public void avgPixs(int x, int y) {
    int avgR = 0;
    int avgG = 0;
    int avgB = 0;
    for (int i = x-1; i < x+2; i++) {
      for (int j = y-1; j <y+2; j++){
        Color pix = getPixel(i,j);
        avgR += pix.getRed();
        avgG += pix.getGreen();
        avgB += pix.getBlue();
      }
      setPixel(x,y, new Color(avgR/9, avgG/9, avgB/9));

    }
  }


}