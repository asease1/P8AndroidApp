package com.p82018.sw806f18.p8androidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    WebView hubView;
    /**
     * Sets whether the URL is loaded the first time or not
     */
    private boolean mbURLLoaded = false;

    private String msCurrentCaptureFilePath;
    private ArrayList<String> arrayListOfImagePaths = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        hubView = (WebView)findViewById(R.id.hub_view);
        initializeWebView();

        String summary =
                "<!DOCTYPE html>" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<a href=\"http://www.google.com\"> Open Google</a> " +
                "<p id=\"demo\"></p>\n" +
                "<input type=\"submit\" value=\"Submit\" onClick=\"f();\">\n" +
                "<input type=\"submit\" value=\"Take A Image\" onClick=\"takePicture();\">\n" +
                "<script>\n" +
                        "function f(){window.location = \"http://www.google.com\"}\n" +
                        "function takePicture() {\n" +
                        "    if(typeof AndroidDevice !== \"undefined\"){\n" +
                        "      AndroidDevice.takePicture();\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "function showPictures() {\n" +
                        "    if(typeof AndroidDevice !== \"undefined\"){\n" +
                        "      AndroidDevice.showPictures();\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "  function imageData(data){\n" +
                        "    document.getElementById('displayImage').setAttribute( 'src', 'data:image/png;base64,'+data );\n" +
                        "    if(typeof AndroidDevice !== \"undefined\"){\n" +
                        "    }"+
                        "}"+
                "\n" +
                "</script> \n" +
                "\n" +
                "</body>\n" +
                "</html>";
        /*String testSite = "<!DOCTYPE html><html><head><title>Barn 0-5 år</title><meta charset='UTF-8' content='width=device-width, initial-scale=1.0'><!-- Latest compiled and minified CSS -->\n" +
                "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" type=\"text/css\">\n" +
                "\n" +
                "<!-- jQuery library -->\n" +
                "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\" type='text/javascript'></script>\n" +
                "\n" +
                "<!-- Popper JS -->\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\" type='text/javascript'></script>\n" +
                "\n" +
                "<!-- Latest compiled JavaScript -->\n" +
                "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js\" type='text/javascript'></script></head><body style='background-color:#D0ECE7;'><div class='content' style='padding:1%;'><h2>Barn 0-5 år</h2><br /><h4>Dette spørgeskema vedrører dit barn.</h4><hr /><form action='http://temp.dk'><h4>Barents højde</h4><p>Skriv barnets højde i centimeter:</p><input id='Barents_højde' type='number' class='form-control'><br /><br /><h4>Barnets vægt</h4><p>Skriv barents vægt i gram:</p><input id='Barnets_vægt' type='number' class='form-control'><br /><br /><h4>Barnets helbred</h4><p>Hvor mange gange har din barn været syg inden for de sidste 6 måneder:</p><select id='Barnets_helbred' class='form-control'><option value='0' class='form-control'>0</option><option value='1-5' class='form-control'>1-5</option><option value='6-10' class='form-control'>6-10</option><option value='10+' class='form-control'>10+</option></select><br /><br /><h4>Barnets livret</h4><p>Udfyld kun hvis relevant:</p><input id='Barnets_livret' type='text' class='form-control'><br /><br /></form></div></body></html>";
        hubView.loadData(testSite, "text/html; charset=UTF-8", null);*/
        //hubView.loadUrl("https://stackoverflow.com/questions/7305089/how-to-load-external-webpage-inside-webview");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hubView.loadUrl("http://"+input.getText()+":8081/api/participant/login");
                hubView.getUrl();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    @Override
    public void onBackPressed() {
        if(hubView.canGoBack())
        {
            hubView.goBack();
            return;
        }
        super.onBackPressed();
    }

    public void initializeWebView() {
        WebSettings webSettings = hubView.getSettings();

        hubView.addJavascriptInterface(new MainActivity.WebViewCameraInterface(), "AndroidDevice");
        hubView.setClickable(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setLightTouchEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setBuiltInZoomControls(false);

        /*Apparently:
        setLoadWithOverviewMode(true) loads the WebView completely zoomed out
        setUseWideViewPort(true) makes the Webview have a normal viewport (such as a normal desktop browser),
        while when false the webview will have a viewport constrained to its own dimensions
        (so if the webview is 50px*50px the viewport will be the same size)*/
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(false);

        // these settings speed up page load into the webview
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        hubView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mbURLLoaded = true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                hubView.loadUrl(url);
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });

		/*
         * Setup the Web Chrome client. Right now, we don't have anything in this class, but we set
		 * it up because we need the JS to have the ability to show alerts
		 */
        hubView.setWebChromeClient(new WebChromeClient());
        hubView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final android.webkit.JsResult result) {
                new AlertDialog.Builder(view.getContext()).setTitle("JS Dialog")
                        .setMessage(message).setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).setCancelable(false).create().show();
                return true;
            }

        });

        loadURL();


    }


    /**
     * Form the file name for the picture. The specific format is:
     * the system timestamp
     *
     * @return The file name in the specified format     *
     */
    private String getFileNameForPicture() {
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString() + ".jpg";
        return ts;
    }

    /**
     * Implicit Intent to get the camera to click images
     */
    public void captureImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Create the file for storing the image
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/WebviewCameraDemo/");
        // Create file directory if it doesn't exist
        if (!directory.exists()) {
            boolean isDirectoryCreated = directory.mkdir();
        }

        File pictureFile = new File(directory, getFileNameForPicture());
        try {
            if (!pictureFile.exists()) {
                pictureFile.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Store the path for later use on Activity Result
        msCurrentCaptureFilePath = pictureFile.getAbsolutePath();

        //Set the path to store the clicked picture
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pictureFile));
        //cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivityForResult(cameraIntent, Constants.REQ_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.REQ_CAMERA:
                if (resultCode == Activity.RESULT_OK) {

                    Bitmap viewBitmap = null;
                    try {
                        viewBitmap = Util.convertImageFileToBitmap(msCurrentCaptureFilePath, Constants.IMAGE_COMPRESSED_WIDTH, Constants.IMAGE_COMPRESSED_HEIGHT);
                        OutputStream outStream = new FileOutputStream(msCurrentCaptureFilePath);
                        viewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, outStream);
                        outStream.close();
                        outStream = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Reset the current capture path
                    msCurrentCaptureFilePath = "";

                } else {
                    File file = new File(msCurrentCaptureFilePath);
                    boolean isDeleted = file.delete();

                }
                AlertDialog.Builder alertClickImages = new AlertDialog.Builder(this);
                alertClickImages.setMessage("Click More Images?");
                alertClickImages.setCancelable(true);

                alertClickImages.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                captureImage();
                            }
                        });

                alertClickImages.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alertClickImages.show();
                break;
            case Constants.REQ_GALLERY:
                if (resultCode == Constants.RESULT_GALLERY_UPLOAD) {
                    String imagePath = data.getStringExtra(Constants.PATH_OF_IMAGE_TO_UPLOAD);

                    //Add it to the  list to maintain all the images that are being uploaded
                    arrayListOfImagePaths.add(imagePath);

                    // Get the image data in a byte array
                    File imageFile = new File(imagePath);
                    Bitmap bitmap = Util.convertImageFileToBitmap(imageFile.getAbsolutePath(), Constants.IMAGE_COMPRESSED_WIDTH, Constants.IMAGE_COMPRESSED_HEIGHT);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] bytes = stream.toByteArray();

                    // get the base 64 string of the image which was converted to a byte array above
                    final String imgBase64String = Base64.encodeToString(bytes, Base64.NO_WRAP);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hubView.loadUrl("javascript:imageData('" + imgBase64String + "')");
                        }
                    });
                }
                break;
        }
    }

    /**
     * Load the URL
     *
     */
    private void loadURL() {
        if (!mbURLLoaded)
            hubView.loadUrl("file:///android_asset/index.html");

    }

    //////////////////////////////////////// INNER CLASSES /////////////////////////////////////////
    public class WebViewCameraInterface {
        /**
         * Javacript function to start native camera
         */
        @JavascriptInterface
        public void takePicture() {
            captureImage();
        }

        /**
         * Javascript function to start the GalleryActivity for user to choose the  image to be uploaded
         */
        @JavascriptInterface
        public void showPictures() {
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivityForResult(intent, Constants.REQ_GALLERY);
        }

    }
}
