
//**********************************************************/
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.lang.String;

import javax.imageio.ImageIO;

class CourseProject extends Frame { // controlling class
	BufferedImage originalImage;
	BufferedImage originalImage2;
	BufferedImage originalImage3;
	BufferedImage originalImage4;
	BufferedImage originalImage5;
	BufferedImage originalImage6;
	BufferedImage originalImage7;
	BufferedImage originalImage8;
	BufferedImage originalImage9;
	
	BufferedImage grayscale;
	BufferedImage grayscale2;
	BufferedImage grayscale3;
	BufferedImage grayscale4;
	BufferedImage grayscale5;
	BufferedImage grayscale6;
	BufferedImage grayscale7;
	BufferedImage grayscale8;
	BufferedImage grayscale9;

	BufferedImage denoise;
	BufferedImage denoise2;
	BufferedImage denoise3;
	BufferedImage denoise4;
	BufferedImage denoise5;
	BufferedImage denoise6;
	BufferedImage denoise7;
	BufferedImage denoise8;
	BufferedImage denoise9;
	
	BufferedImage noiseImage;
	BufferedImage noiseImage2;
	BufferedImage noiseImage3;
	BufferedImage noiseImage4;
	BufferedImage noiseImage5;
	BufferedImage noiseImage6;
	BufferedImage noiseImage7;
	BufferedImage noiseImage8;
	BufferedImage noiseImage9;

	BufferedImage subtractedImage;
	BufferedImage subtractedImage2;
	BufferedImage subtractedImage3;
	BufferedImage subtractedImage4;
	BufferedImage subtractedImage5;
	BufferedImage subtractedImage6;
	BufferedImage subtractedImage7;
	BufferedImage subtractedImage8;
	BufferedImage subtractedImage9;


	int width, width1;
	int height, height1;

	public CourseProject() {
		// constructor
		// Get an image from the specified file in the current directory on the
		// local hard disk.
		try {
			originalImage = ImageIO.read(new File("unmorphedimageset1.png"));
			originalImage2 = ImageIO.read(new File("morphedimageset1.png"));
			originalImage3 = ImageIO.read(new File("unmorphedimage2set1.png"));
			originalImage4 = ImageIO.read(new File("unmorphedimageset2.png"));
			originalImage5 = ImageIO.read(new File("morphedimageset2.png"));
			originalImage6 = ImageIO.read(new File("unmorphedimage2set2.png"));
			originalImage7 = ImageIO.read(new File("unmorphedimageset3.png"));
			originalImage8 = ImageIO.read(new File("morphedimageset3.png"));
			originalImage9 = ImageIO.read(new File("unmorphedimage2set3.png"));

		} catch (Exception e) {
			System.out.println("Cannot load the provided image");
		}
		this.setTitle("Course Project Test");
		this.setVisible(true);

		width = originalImage.getWidth();
		height = originalImage.getHeight();

		grayscale = convertToGrayscale(originalImage);
		grayscale2 = convertToGrayscale(originalImage2);
		grayscale3 = convertToGrayscale(originalImage3);
		grayscale4 = convertToGrayscale(originalImage4);
		grayscale5 = convertToGrayscale(originalImage5);
		grayscale6 = convertToGrayscale(originalImage6);
		grayscale7 = convertToGrayscale(originalImage7);
		grayscale8 = convertToGrayscale(originalImage8);
		grayscale9 = convertToGrayscale(originalImage9);

		double[][] gaussianKernel = generateGaussianKernel(3, 3);
		denoise = applyFilter(grayscale, gaussianKernel);
		denoise2 = applyFilter(grayscale2, gaussianKernel);
		denoise3 = applyFilter(grayscale3, gaussianKernel);
		denoise4 = applyFilter(grayscale4, gaussianKernel);
		denoise5 = applyFilter(grayscale5, gaussianKernel);
		denoise6 = applyFilter(grayscale6, gaussianKernel);
		denoise7 = applyFilter(grayscale7, gaussianKernel);
		denoise8 = applyFilter(grayscale8, gaussianKernel);
		denoise9 = applyFilter(grayscale9, gaussianKernel);
		
		subtractedImage = subtractImages(grayscale, denoise);
		subtractedImage2 = subtractImages(grayscale2, denoise2);
		subtractedImage3 = subtractImages(grayscale3, denoise3);
		subtractedImage4 = subtractImages(grayscale4, denoise4);
		subtractedImage5 = subtractImages(grayscale5, denoise5);
		subtractedImage6 = subtractImages(grayscale6, denoise6);
		subtractedImage7 = subtractImages(grayscale7, denoise7);
		subtractedImage8 = subtractImages(grayscale8, denoise8);
		subtractedImage9 = subtractImages(grayscale9, denoise9);
		

		// Brighten the noise image for better visibility
		int brightnessFactor = 200;
		noiseImage = adjustBrightness(subtractedImage, brightnessFactor);
		noiseImage2 = adjustBrightness(subtractedImage2, brightnessFactor);
		noiseImage3 = adjustBrightness(subtractedImage3, brightnessFactor);
		noiseImage4 = adjustBrightness(subtractedImage4, brightnessFactor);
		noiseImage5 = adjustBrightness(subtractedImage5, brightnessFactor);
		noiseImage6 = adjustBrightness(subtractedImage6, brightnessFactor);
		noiseImage7 = adjustBrightness(subtractedImage7, brightnessFactor);
		noiseImage8 = adjustBrightness(subtractedImage8, brightnessFactor);
		noiseImage9 = adjustBrightness(subtractedImage9, brightnessFactor);


		// Anonymous inner-class listener to terminate program
		this.addWindowListener(new WindowAdapter() {// anonymous class definition
			public void windowClosing(WindowEvent e) {
				System.exit(0);// terminate the program
			}// end windowClosing()
		}// end WindowAdapter
		);// end addWindowListener
	}// end constructor

	private static BufferedImage adjustBrightness(BufferedImage image, int brightnessFactor) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage brightenedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixelValue = image.getRGB(x, y) & 0xFF;
				int newBrightness = Math.min(255, pixelValue + brightnessFactor); // Increase brightness
				int newPixelValue = (newBrightness << 16) | (newBrightness << 8) | newBrightness;
				brightenedImage.setRGB(x, y, newPixelValue);
			}
		}

		return brightenedImage;
	}

//	https://stackoverflow.com/questions/9131678/convert-a-rgb-image-to-grayscale-image-reducing-the-memory-in-java
	private static BufferedImage convertToGrayscale(BufferedImage originalImage) {
		BufferedImage grayscaleImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		grayscaleImage.getGraphics().drawImage(originalImage, 0, 0, null);
		return grayscaleImage;
	}

//	formula from 
//	https://stackoverflow.com/questions/69643180/java-remove-gaussian-noise
	private static double[][] generateGaussianKernel(int size, double sigma) {
		double[][] kernel = new double[size][size];
		double sum = 0;

		for (int x = -size / 2; x <= size / 2; x++) {
			for (int y = -size / 2; y <= size / 2; y++) {
				kernel[x + size / 2][y + size / 2] = (1 / (2 * Math.PI * sigma * sigma))
						* Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
				sum += kernel[x + size / 2][y + size / 2];
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				kernel[i][j] /= sum;
			}
		}
		return kernel;
	}

	// https://stackoverflow.com/questions/69643180/java-remove-gaussian-noise
	private static BufferedImage applyFilter(BufferedImage image, double[][] filter) {
		int width = image.getWidth();
		int height = image.getHeight();
		int filterSize = filter.length;

		BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double sum = 0;

				for (int i = 0; i < filterSize; i++) {
					for (int j = 0; j < filterSize; j++) {
						int pixelX = Math.min(Math.max(x + j - filterSize / 2, 0), width - 1);
						int pixelY = Math.min(Math.max(y + i - filterSize / 2, 0), height - 1);

						sum += (image.getRGB(pixelX, pixelY) & 0xFF) * filter[i][j];
					}
				}

				int newValue = (int) Math.round(sum);
				int pixelValue = (newValue << 16) | (newValue << 8) | newValue;
				filteredImage.setRGB(x, y, pixelValue);
			}
		}

		return filteredImage;
	}

	private static BufferedImage subtractImages(BufferedImage image1, BufferedImage image2) {
		int width = image1.getWidth();
		int height = image1.getHeight();
		BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int pixelValue1 = image1.getRGB(x, y) & 0xFF;
				int pixelValue2 = image2.getRGB(x, y) & 0xFF;

				int newValue = Math.abs(pixelValue1 - pixelValue2); // Calculate noise as the absolute difference
				int pixelValue = (newValue << 16) | (newValue << 8) | newValue;
				resultImage.setRGB(x, y, pixelValue);
			}
		}

		return resultImage;
	}

	private int clip(int v) {
		v = v > 255 ? 255 : v;
		v = v < 0 ? 0 : v;
		return v;
	}

	protected int getRed(int pixel) {
		return (pixel >>> 16) & 0xFF;
	}

	protected int getGreen(int pixel) {
		return (pixel >>> 8) & 0xFF;
	}

	protected int getBlue(int pixel) {
		return pixel & 0xFF;
	}

	public void paint(Graphics g) {
		int w = width / 2; // door image
		int h = height / 2;

		int w1 = width1 / 2; // statue
		int h1 = height1 / 2;

		this.setSize(w * 5 + 100, h * 4 + 50);

		g.setColor(Color.BLACK);
		Font f1 = new Font("Verdana", Font.PLAIN, 13);
		g.setFont(f1);

		g.drawImage(originalImage, 20, 50, w, h, this);
		g.drawImage(denoise, 50 + w, 50, w, h, this);
		g.drawImage(noiseImage, 80 + w * 2, 50, w, h, this);

		g.drawString("Original", 20, 40);
		g.drawString("Denoised", 50 + w, 40);
		g.drawString("Noise from Gaussian", 80 + w * 2, 40);

	}
	// =======================================================//

	public static void main(String[] args) {

		CourseProject img = new CourseProject();// instantiate this object
		img.repaint();// render the image

	}// end main
}
//=======================================================//