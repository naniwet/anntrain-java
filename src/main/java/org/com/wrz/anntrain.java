package org.com.wrz;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.opencv.core.*;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.Ml;
import org.opencv.ml.TrainData;


public class anntrain {
    static {
        //加载opencv动态链接库
        String path = "D:/opencv3.4/opencv/build/java/x86/opencv_java341.dll";
        System.load(path);
    }
    /*
     * 样本数据
     * */
    private static List<String> characters=getCharacters("res/train/data");

    private int class_num=5;

    private FeatureInterface feature=new Features();


    private static char[] KChars = {'0','1','2','3','4','5'};


    private ANN_MLP mlp=ANN_MLP.create();
    /**
     *读取图片
     *设置训练数据Mat输入
     * */
    public TrainData readImage(int numberforcount){
		/*
		 * 读取每个类文件夹下所有图片
		 * */
        List<String> paths=new ArrayList();

        Mat samples=new Mat();
        Vector<Integer> labels=new Vector<Integer>();

        String path="res/train/data";
        getDir(path,paths);
        for(int i=0;i<paths.size();i++){
            char char_key = KChars[i];
            String sub_folder= path+"/"+char_key+"/train";
            List<String> listfile = getAllFiles(sub_folder);
            int listsize = listfile.size();
            System.out.println( ">> Characters count before:"+listsize);  //文件夹下文件数量
            List<Mat> matVec=new ArrayList<Mat>(numberforcount);
            // matVec.reserve(number_for_count);    //样本数量
            for (String file : listfile) {
                Mat img = Imgcodecs.imread(file, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);  // a grayscale image
                matVec.add(img);
            }
            for (int t = 0; t <  numberforcount -  listsize; t++) {
                int rand_range = listsize + t;
                int ran_num = (int)Math.random() % rand_range;
                Mat img = matVec.get(ran_num);
                Mat simg = getSyntheticImage(img);
                matVec.add(simg);
                Imgcodecs.imwrite(sub_folder +"/" + i + "_" + t +"_" + ran_num + ".jpg", simg);

            }

            for (Mat img : matVec) {
                Mat fps = feature.getLBPFeatures(img);

                samples.push_back(fps);
                labels.add(i);
            }

        }
        Mat samples_=new Mat();
        samples.convertTo(samples_, CvType.CV_32F);
        Mat train_classes = Mat.zeros((int) labels.size(), class_num, CvType.CV_32F);

        for (int i = 0; i < train_classes.rows(); ++i) {
            train_classes.put(i, labels.get(i),1.f) ;
        }

        return TrainData.create(samples_, 0, train_classes);
    }


    private Mat getSyntheticImage( Mat image) {
        Random random=new Random();
        int rand_type =  random.nextInt(100);


        Mat result = image.clone();

        if (rand_type % 2 == 0) { //上下移动
            int ran_x = random.nextInt(10) % 5 - 2;
            int ran_y = random.nextInt(10) % 5 - 2;
            result = Creat_Data.translateImg(result, ran_x, ran_y,255);
        } else if (rand_type % 2 != 0) { //旋转
            float angle = (float)(random.nextInt(360) % 360 - 5);

            result = Creat_Data.rotateImg(result, angle,255 );
        }

        return result;
    }



    /**
     * 训练数据
     * */
    public void trainData(String xml){

        Mat layers=new Mat();
        layers.create(1, 3, CvType.CV_32SC1);
        int layernum[]={512,40,class_num};
        layers.put(0,0,layernum);

        mlp.setLayerSizes(layers);
        mlp.setActivationFunction(ANN_MLP.SIGMOID_SYM);
        mlp.setTrainMethod(ANN_MLP.BACKPROP);
        mlp.setTermCriteria(new TermCriteria(1, 30000, 0.0001));
        mlp.setBackpropWeightScale(0.1);
        mlp.setBackpropMomentumScale(0.1);
		/*
		 * 开始训练
		 * */
        long start = System.currentTimeMillis();

        TrainData traindata = readImage(300);
        mlp.train(traindata);
        long end = System.currentTimeMillis();
        System.out.println("Training done. Time elapse: " + (double) (end - start) / (1000) +"second");
        mlp.save(xml);
        mlp.clear();
        System.out.println("layer:"+layers.dump());

    }
    /**
     * 识别
     * */
    public char predict(String xml,String image){
        ANN_MLP ann = ANN_MLP.load(xml);

        Mat image_src=Imgcodecs.imread(image,Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat image_temp= feature.getLBPFeatures(image_src);


        Mat predict=new Mat(1,class_num,CvType.CV_32FC1);

        ann.predict(image_temp, predict,ANN_MLP.UPDATE_MODEL);
//        System.out.println("sample--"+image_temp.dump());
        System.out.println("outputs:"+predict.dump());

        MinMaxLocResult maxLoc = Core.minMaxLoc(predict);
        System.out.println(maxLoc.maxLoc+"---"+maxLoc.minVal);
        System.out.println("character:"+characters.get((int)maxLoc.maxLoc.x));
        char result=characters.get((int)maxLoc.maxLoc.x).charAt(0);
        return result;

    }
    public static void main(String[] args) {

        anntrain num=new anntrain();
        String xml="C:/Users/gpsts1/Desktop/PCB_ANN/src/main/java/mlp.xml";
        num.readImage(300);
        num.trainData(xml);
        int corrects = 0;
        int sum = 0;
        String path="C:\\Users\\gpsts1\\Desktop\\PCB_ANN\\res\\train\\data";
        List<String> paths=new ArrayList();
        getDir(path,paths);
        for(int i=0;i<paths.size();i++) {
            char char_key = KChars[i];
            String sub_folder= path+"/"+char_key+"/test";
            List<String> listfile = getAllFiles(sub_folder);
            int listsize = listfile.size();
            System.out.println( ">> Characters count now :"+listsize);  //文件夹下文件数量
            for (String file : listfile) {
                if(num.predict(xml, file)==(char_key))
                    corrects++;
                sum++;
            }
        }
        float rate = (float) corrects / (sum == 0 ? 1 : sum);
        System.out.println(corrects);
        System.out.println(sum);
        System.out.println(rate);


//        num.predict(xml, "C:\\Users\\gpsts1\\Desktop\\PCB_ANN\\res\\train\\test\\71.jpg");
    }
    public static List<String> getCharacters(String path){
        List<String> list=new ArrayList();
        File file=new File(path);
        File[] files = file.listFiles();
        for(File fi:files){
            if(fi.isDirectory()){
                list.add(fi.getName());
            }
        }
        return list;
    }
    public static void getDir(String path,List<String> list){
        File file=new File(path);
        File[] files = file.listFiles();
        for(File fi:files){
            if(fi.isDirectory()){
                list.add(fi.getPath());
            }
        }
    }
    public static List<String> getAllFiles(String path){
        List<String> filepath=new ArrayList();
        File file=new File(path);
        File[] files = file.listFiles();
        for(File f:files){
            if(f.isDirectory()){

            }else{
                filepath.add(f.getPath());
            }
        }
        return filepath;
    }
}
