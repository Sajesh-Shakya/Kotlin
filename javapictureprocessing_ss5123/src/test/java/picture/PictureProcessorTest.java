package picture;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class PictureProcessorTest {

  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Test
  public void invertBlack() throws IOException {
    Assert.assertEquals(
        new Picture("images/white64x64.png"),
        TestSuiteHelper.runMain(tmpFolder, "invert", "images/black64x64.png"));
  }

  @Test
  public void invertRainbow() throws IOException {
    Picture p1 = new Picture("images/rainbow64x64doc.png");
    Picture p2 = new Picture("images/rainbowI64x64doc.png");
    p1.invert();
    System.out.println(p1);
    System.out.println("break");
    System.out.println(p2);
    Assert.assertEquals(p1, p2);

    Assert.assertEquals(
        new Picture("images/rainbowI64x64doc.png"),
        TestSuiteHelper.runMain(tmpFolder, "invert", "images/rainbow64x64doc.png"));
  }

  @Test
  public void grayscaleBlack() throws IOException {
    Assert.assertEquals(
        new Picture("images/black64x64.png"),
        TestSuiteHelper.runMain(tmpFolder, "grayscale", "images/black64x64.png"));
  }

  @Test
  public void grayscaleRainbow() throws IOException {
    Assert.assertEquals(
        new Picture("images/rainbowGS64x64doc.png"),
        TestSuiteHelper.runMain(tmpFolder, "grayscale", "images/rainbow64x64doc.png"));
  }

  @Test
  public void rotate90Green() throws IOException {
    Picture p1 = new Picture( "images/green64x64doc.png");
    Picture p2 = new Picture("images/green64x64R90doc.png");
    p1.rotate("90");
    System.out.println(p1);
    System.out.println(p1.getWidth());
    System.out.println(p1.getHeight());
    System.out.println("break");
    System.out.println(p2);
    System.out.println(p2.getWidth());
    System.out.println(p2.getHeight());
    Assert.assertEquals(p1, p2);

    Assert.assertEquals(
        new Picture("images/green64x64R90doc.png"),
        TestSuiteHelper.runMain(tmpFolder, "rotate", "90", "images/green64x64doc.png"));
  }

  @Test
  public void rotate90BlueRect() throws IOException {
    Assert.assertEquals(
        new Picture("images/blueR9064x32doc.png"),
        TestSuiteHelper.runMain(tmpFolder, "rotate", "90", "images/blue64x32doc.png"));
  }

  @Test
  public void rotate180BlueRect() throws IOException {
    Assert.assertEquals(
        new Picture("images/blueR18064x32doc.png"),
        TestSuiteHelper.runMain(tmpFolder, "rotate", "180", "images/blue64x32doc.png"));
  }

  @Test
  public void rotate270BlueRect() throws IOException {
    Assert.assertEquals(
        new Picture("images/blueR27064x32doc.png"),
        TestSuiteHelper.runMain(tmpFolder, "rotate", "270", "images/blue64x32doc.png"));
  }

  @Test
  public void flipVGreen() throws IOException {
    Assert.assertEquals(
        new Picture("images/green64x64FVdoc.png"),
        TestSuiteHelper.runMain(tmpFolder, "flip", "V", "images/green64x64doc.png"));
  }

  @Test
  public void flipVBlue() throws IOException {
    Assert.assertEquals(
        new Picture("images/blueFV64x32doc.png"),
        TestSuiteHelper.runMain(tmpFolder, "flip", "V", "images/blue64x32doc.png"));
  }

  @Test
  public void flipHBlue() throws IOException {
    Assert.assertEquals(
        new Picture("images/blueFH64x32doc.png"),
        TestSuiteHelper.runMain(tmpFolder, "flip", "H", "images/blue64x32doc.png"));
  }

  @Test
  public void blurBWPatterns() throws IOException {
    Assert.assertEquals(
        new Picture("images/bwpatternsblur64x64.png"),
        TestSuiteHelper.runMain(tmpFolder, "blur", "images/bwpatterns64x64.png"));
  }

  @Test
  public void blurSunset() throws IOException {
    Assert.assertEquals(
        new Picture("images/sunsetBlur64x32.png"),
        TestSuiteHelper.runMain(tmpFolder, "blur", "images/sunset64x32.png"));
  }

  @Test
  public void blendBWAndRainbow() throws IOException {
    Assert.assertEquals(
        new Picture("images/rainbowpatternsblend64x64.png"),
        TestSuiteHelper.runMain(
            tmpFolder, "blend", "images/bwpatterns64x64.png", "images/rainbow64x64doc.png"));
  }

  @Test
  public void blendRainbowSunset() throws IOException {
    Assert.assertEquals(
        new Picture("images/rainbowsunsetBlend.png"),
        TestSuiteHelper.runMain(
            tmpFolder, "blend", "images/rainbow64x64doc.png", "images/sunset64x32.png"));
  }
}
