/*
* https://www.imageeprocessing.com/2017/12/k-means-clustering-on-rgb-image.html#
* ^This type of explanation really helps to develop the implementation of such Algorithms, a BIG thumbs up;)
* */
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Engine {
    int [][] clusterMap;
    double[] clusters;
    int k,height,width;
    boolean goAgain=true;
    BufferedImage testImage=null;
    Color[] colours=new Color[10];
    Random rd=new Random();

    public void genColours(){
        colours[0]= new Color(255,0,0); colours[2]= new Color(0,255,0);
        colours[1]= new Color(0,0,255);colours[3]= new Color(225,237,3);
        colours[4]= new Color(125,3,255);colours[5]= new Color(64,255,255);
        colours[6]= new Color(255,66,125);colours[7]= new Color(255,66,0);
        colours[8]= new Color(255,255,245);colours[9]= new Color(0,0,0);
    }

    public void kun() throws IOException {
        Color col;
        Random rd=new Random();
        int rnd,cnt=0;
        boolean b;
        testImage=ImageIO.read(new File("F:\\Codes_misc\\ibtd\\1002.jpg"));
        height=testImage.getHeight();
        width=testImage.getWidth();
        clusterMap=new int[width][height];
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter k value");
        k=sc.nextInt();
        clusters = new double[4*k];

        for(int i=0;i<k;i++) {
            clusters[i*4] = (i+1);
        }

        for(int i=0;i<clusters.length;i++){

            rnd=rd.nextInt(255);
            if(i%4==0) continue;
            else
                clusters[i]=rd.nextInt(255);
        }
        for(int i=0;i<k*4;i++) {
            System.out.println(clusters[i]);
        }
        genColours();

        while (goAgain) {
            for (int i = width - 1; i >= 0; i--) {
                for (int j = height - 1; j >= 0; j--) {
                    col = new Color(testImage.getRGB(i, j));
                    clusterMap[i][j] = distanceMinimal(col);
                    //System.out.println(clusterMap[i][j]);
                }
                //System.out.println("\n");
            }
            isClustered();
        }
        BufferedImage outImg=null;
        outImg= new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for (int i = width - 1; i >= 0; i--) {
            for (int j = height - 1; j >= 0; j--) {
                outImg.setRGB(i,j,colours[clusterMap[i][j]-1].getRGB());
            }
        }
        File outputFile=null;
        outputFile = new File("F:\\Codes_misc\\Processed\\0001.jpg");
        ImageIO.write(outImg, "jpg", outputFile);
    }

    public int distanceMinimal(Color col){

        double minDistance=99999;
        int cn=1;
        double distance;
        for(int i=0;i<=clusters.length-3;i+=4){

            distance= Math.sqrt(Math.pow(clusters[i+1]-col.getRed(),2)+Math.pow(clusters[i+2]-col.getGreen(),2)+Math.pow(clusters[i+3]-col.getBlue(),2));

            if(distance<minDistance) {
                minDistance=distance;
                cn = (int) clusters[i];
            }
        }

        return cn;
    }
    public boolean isClustered(){
        double [][] clusterRGBcnt= new double[4][k+1];
        Color clr;
        double dr,dg,db,thrshold=0.00001;

        for(int i=width-1; i>=0; i--){
            for(int j=height-1; j>=0; j--){
                clr=new Color(testImage.getRGB(i,j));
                clusterRGBcnt[0][clusterMap[i][j]]++;
                clusterRGBcnt[1][clusterMap[i][j]]+=clr.getRed();
                clusterRGBcnt[2][clusterMap[i][j]]+=clr.getGreen();
                clusterRGBcnt[3][clusterMap[i][j]]+=clr.getBlue();
            }
        }
        for(int i=1;i<k+1;i++) {
            if(clusterRGBcnt[0][i]==0){
                int j=(i-1)*4;
                clusters[j+1]=rd.nextInt(255);
                clusters[j+2]=rd.nextInt(255);
                clusters[j+3]=rd.nextInt(255);
                return false;
            }
        }
        for(int i=1;i<4;i++){
            for(int j=1;j<k+1;j++){
                System.out.println(clusterRGBcnt[0][j]);
                clusterRGBcnt[i][j]/=clusterRGBcnt[0][j];
                System.out.println(clusterRGBcnt[i][j]);
            }
            System.out.println("\n");
        }
        for(int i=0;i<=clusters.length-3;){

            dr=Math.abs(clusters[i+1]-clusterRGBcnt[1][(int)clusters[i]]);
            dg=Math.abs(clusters[i+2]-clusterRGBcnt[2][(int)clusters[i]]);
            db=Math.abs(clusters[i+3]-clusterRGBcnt[3][(int)clusters[i]]);
            clusters[i+1]=clusterRGBcnt[1][(int)clusters[i]];
            clusters[i+2]=clusterRGBcnt[2][(int)clusters[i]];
            clusters[i+3]=clusterRGBcnt[3][(int)clusters[i]];

            if(dr<thrshold && dg <thrshold && db <thrshold)
                goAgain=false;
            i+=4;
        }
        return goAgain;
    }
}
