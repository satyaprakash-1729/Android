#include <com_example_mathayus_myapplicationlearn_opncvNative.h>

JNIEXPORT jint JNICALL Java_com_example_mathayus_myapplicationlearn_opncvNative_convertGray
  (JNIEnv *, jclass, jlong addrRgba){
    Mat& frame = *(Mat*)addrRgba;

    return detect(frame);
  }

int detect(Mat& frame){
    String face_cascade_name = "/storage/emulated/0/Download/haarcascade_frontalface_alt.xml";
     String eyes_cascade_name = "/storage/emulated/0/Download/haarcascade_eye_tree_eyeglasses.xml";
     CascadeClassifier face_cascade;
     CascadeClassifier eyes_cascade;

     if( !face_cascade.load( face_cascade_name ) ){ printf("--(!)Error loading\n"); return -1; };
     if( !eyes_cascade.load( eyes_cascade_name ) ){ printf("--(!)Error loading\n"); return -1; };
     std::vector<Rect> faces;
       Mat frame_gray;

       cvtColor( frame, frame_gray, CV_BGR2GRAY );
       equalizeHist( frame_gray, frame_gray );

       //-- Detect faces
       face_cascade.detectMultiScale( frame_gray, faces, 1.3, 4, 0|CV_HAAR_SCALE_IMAGE, Size(20, 20) );

       for( size_t i = 0; i < faces.size(); i++ )
       {
         //Point center( faces[i].x + faces[i].width*0.5, faces[i].y + faces[i].height*0.5 );
         //ellipse( frame, center, Size( faces[i].width*0.5, faces[i].height*0.5), 0, 0, 360, Scalar( 255, 0, 255 ), 4, 8, 0 );
        rectangle(frame, Point(faces[i].x,faces[i].y),Point(faces[i].x + faces[i].width,faces[i].y + faces[i].height),Scalar(255,0,255), 2, 8, 0);
         Mat faceROI = frame_gray( faces[i] );
         std::vector<Rect> eyes;

         //-- In each face, detect eyes
         eyes_cascade.detectMultiScale( faceROI, eyes, 1.1, 5, 0 |CV_HAAR_SCALE_IMAGE, Size(15, 15), Size(40,40) );

         for( size_t j = 0; j < eyes.size(); j++ )
          {
            Point center( faces[i].x + eyes[j].x + eyes[j].width*0.5, faces[i].y + eyes[j].y + eyes[j].height*0.5 );
            int radius = cvRound( (eyes[j].width + eyes[j].height)*0.25 );
            circle( frame, center, radius, Scalar( 255, 0, 0 ), 4, 8, 0 );
          }
       }
        return 0;
}
