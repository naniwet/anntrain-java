package org.com.wrz;

public class App {
    static {
        //加载opencv动态链接库
        String path = "D:/opencv3.4/opencv/build/java/x86/opencv_java341.dll";
        System.load(path);
    }


    public static void main(String[] args)  {


       // Mat src = Imgcodecs.imread("res/10.jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE); //读取原始电路板图片
//        ann ann=new ann("res/train/data", "res/train/ann.xml");
//        ann.train();
        anntrain ann = new anntrain();
        String xml="C:/Users/gpsts1/Desktop/PCB_ANN/src/main/java/mlp.xml";
        String path="C:\\Users\\gpsts1\\Desktop\\PCB_ANN\\res\\train\\data";
        ann.trainData(xml);
        ann.predictdata(xml,path);

/*     ResLocate resLocate = new ResLocate();
        resLocate.resLocate(src);
       ResIdentify resIdentify = new ResIdentify();
        resIdentify.resIdentify(src);
        SVMTrain svm = new SVMTrain();
       svm.svmTrain(true, false);*/

     /*   CapLocate capLocate = new CapLocate();
        capLocate.capLocate(src);*/

  /* Mat detect = Imgcodecs.imread("res/img/capacity/debug_crop_19.jpg");
     PolarDetect polarDetect = new PolarDetect();
     polarDetect.detect(detect);*/
      //  Mat detect = Imgcodecs.imread("res/img/jibian1.jpg");
      //  Distortion distortion = new Distortion(detect);

/*
        MissParts missParts = new MissParts();
        missParts.subtract();*/


/*     RandomAccessFile file=new RandomAccessFile("/home/zydq/Image2/Texture/Train.txt","rw");
      file.seek(file.length()-1);

       for(int i=0;i<=95;i++){
           file.writeChars("/home/zydq/Image2/1/"+i+".jpg 1");
           file.writeChars("\n");

       }
        file.close();*/
    }

}

