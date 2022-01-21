package robatortas.code.files;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Noise {

	public double values[];
	
	public static int w,h;
	
	public Random random = new Random();
	
	//My representation of the noise channels
	/* Noise Channels
       sample:    A  B      |1
       half:     F      H   |1.5
       sample:    C         |2
       half:         D      |2.5
       sample:       E      |3
       half:                |3.5
       sample        G      |4
    ------------------------|
    X             1  2  3  4 Y
                   .5 .5 .5
	 */
	
	public Noise(int w, int h, int sampleSize) {
		Noise.w=w;
		Noise.h=h;
		values = new double[w*h];
		for(int y=0;y<w;y+=sampleSize) {
			for(int x=0;x<w;x+=sampleSize) {
				setSample(x,y,random.nextFloat()*2-1);
			}
		}
		int sample = sampleSize;
		double scale=1.0/w;
		while(sample>1) {
			System.out.println("Calculating...");
			int halfSample=sample/2;
			for(int y=0;y<w;y+=sample) {
				for(int x=0;x<w;x+=sample) {
					double a=sample(x,y);
					double b=sample(x+sample,y);
					double c=sample(x,y+sample);
					double d=sample(x+sample,y+sample);
					
					double e=(a+b+c+d)/4.0+(random.nextFloat())*sample*scale;
					setSample(x+halfSample,y+halfSample,e);
				}
			}
			for(int y=0;y<w;y+=sample) {
				for(int x=0;x<w;x+=sample) {
					double a=sample(x,y);
					double b=sample(x+sample,y);
					double c=sample(x,y+sample);
					double d=sample(x+halfSample,y+halfSample);
					double e=sample(x+halfSample,y-halfSample);
					double f=sample(x-halfSample,y+halfSample);
					
					double H=(a+b+d+e)/4.0+(random.nextFloat()*2-1)*sample*scale;
					double g=(a+c+d+f)/4.0+(random.nextFloat()*2-1)*sample*scale;
					setSample(x+halfSample,y,H);
					setSample(x,y+halfSample,g);
				}
			}
			sample/=2;
		}
	}
	
	public double sample(int x, int y) {
		return values[(x&(w-1))+(y&(h-1))*w];
	}
	
	public void setSample(int x, int y, double value) {
		values[(x&(w-1))+(y&(h-1))*w] = value;
	}

	public static int width = 128;
	public static int height = 128;
	
	public static void main(String[] args) {
		while(true) {
		System.out.println("Generating Noise Map...");
		
		Noise noiseMap = new Noise(width,height,16);
		
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		int[] pixels = new int[width*height];
		
		for(int i=0;i<w*h;i++) {
			int result=(int)(noiseMap.values[i]*120+128);
			pixels[i]=result<<16|result<<8|result;
		}
		
		for(int y=0;y<height;y++) {
			for(int x=0;x<width;x++) {
				int i = x+y*width;
			}
		}
		image.setRGB(0,0,width,height,pixels,0,width);
		JOptionPane.showMessageDialog(null,null,"Generate",JOptionPane.YES_NO_OPTION,new ImageIcon(image.getScaledInstance(width*4,height*4,Image.SCALE_AREA_AVERAGING)));
		}
	}
}
