package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.ContentDetailsInput;
import com.home.apisdk.apiModel.ContentDetailsOutput;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.PPVModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.R.id.list;

/**
 * Created by User on 12-12-2016.
 */
public class GetContentDetailsAsynTask extends AsyncTask<ContentDetailsInput, Void, Void> {

    ContentDetailsInput contentDetailsInput;
    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface GetContentDetails {
        void onGetContentDetailsPreExecuteStarted();

        void onGetContentDetailsPostExecuteCompleted(ContentDetailsOutput contentDetailsOutput, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetContentDetails listener;
    private Context context;
    ContentDetailsOutput contentDetailsOutput = new ContentDetailsOutput();

    public GetContentDetailsAsynTask(ContentDetailsInput contentDetailsInput, GetContentDetails listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.contentDetailsInput = contentDetailsInput;
      PACKAGE_NAME = context.getPackageName();
        Log.v("SUBHA", "pkgnm :" + PACKAGE_NAME);
        Log.v("SUBHA", "GetContentListAsynTask");


    }

    @Override
    protected Void doInBackground(ContentDetailsInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getContentDetailsUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.contentDetailsInput.getAuthToken());
            httppost.addHeader("permalink", this.contentDetailsInput.getPermalink());

            if (this.contentDetailsInput.getUser_id() != null) {
                httppost.addHeader("user_id", this.contentDetailsInput.getUser_id());
            }else{
                httppost.addHeader("user_id", "");
            }

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());


            } catch (org.apache.http.conn.ConnectTimeoutException e) {

                status = 0;
                message = "Error";


            } catch (IOException e) {
                status = 0;
                message = "Error";
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
            }

            if (status > 0) {

                if (status == 200) {

                    JSONObject mainJson = myJson.getJSONObject("movie");

                    if ((mainJson.has("name")) && mainJson.getString("name").trim() != null && !mainJson.getString("name").trim().isEmpty() && !mainJson.getString("name").trim().equals("null") && !mainJson.getString("name").trim().matches("")) {
                        contentDetailsOutput.setName(mainJson.getString("name"));
                    } else {
                        contentDetailsOutput.setName("");

                    }




                    if ((mainJson.has("is_favorite")) && mainJson.getString("is_favorite").trim() != null && !mainJson.getString("is_favorite").trim().isEmpty() && !mainJson.getString("is_favorite").trim().equals("null") && !mainJson.getString("is_favorite").trim().matches("")) {

                        contentDetailsOutput.setIsFavorite(Integer.parseInt(mainJson.getString("is_favorite")));


                    }

                    if ((mainJson.has("genre")) && mainJson.getString("genre").trim() != null && !mainJson.getString("genre").trim().isEmpty() && !mainJson.getString("genre").trim().equals("null") && !mainJson.getString("genre").trim().matches("")) {
                        contentDetailsOutput.setGenre(mainJson.getString("genre"));


                    } else {
                        contentDetailsOutput.setGenre("");

                    }
                    if ((mainJson.has("censor_rating")) && mainJson.getString("censor_rating").trim() != null && !mainJson.getString("censor_rating").trim().isEmpty() && !mainJson.getString("censor_rating").trim().equals("null") && !mainJson.getString("censor_rating").trim().matches("")) {
                        contentDetailsOutput.setCensorRating(mainJson.getString("censor_rating"));


                    } else {
                        contentDetailsOutput.setCensorRating("");

                    }
                    if ((mainJson.has("story")) && mainJson.getString("story").trim() != null && !mainJson.getString("story").trim().isEmpty() && !mainJson.getString("story").trim().equals("null") && !mainJson.getString("story").trim().matches("")) {
                        contentDetailsOutput.setStory(mainJson.getString("story"));
                    } else {
                        contentDetailsOutput.setStory("");

                    }
                    if ((mainJson.has("is_episode")) && mainJson.getString("is_episode").trim() != null && !mainJson.getString("is_episode").trim().isEmpty() && !mainJson.getString("is_episode").trim().equals("null") && !mainJson.getString("is_episode").trim().matches("")) {
                        contentDetailsOutput.setIsEpiosde(mainJson.getString("is_episode"));
                    } else {
                        contentDetailsOutput.setIsEpiosde("");

                    }
                    if ((mainJson.has("trailerUrl")) && mainJson.getString("trailerUrl").trim() != null && !mainJson.getString("trailerUrl").trim().isEmpty() && !mainJson.getString("trailerUrl").trim().equals("null") && !mainJson.getString("trailerUrl").trim().matches("")) {
                        contentDetailsOutput.setTrailerUrl(mainJson.getString("trailerUrl"));
                    } else {
                        contentDetailsOutput.setTrailerUrl("");

                    }

                    if ((mainJson.has("movie_stream_uniq_id")) && mainJson.getString("movie_stream_uniq_id").trim() != null && !mainJson.getString("movie_stream_uniq_id").trim().isEmpty() && !mainJson.getString("movie_stream_uniq_id").trim().equals("null") && !mainJson.getString("movie_stream_uniq_id").trim().matches("")) {
                        contentDetailsOutput.setMovieStreamUniqId(mainJson.getString("movie_stream_uniq_id"));

                    } else {
                        contentDetailsOutput.setMovieStreamUniqId("");

                    }

                    if ((mainJson.has("muvi_uniq_id")) && mainJson.getString("muvi_uniq_id").trim() != null && !mainJson.getString("muvi_uniq_id").trim().isEmpty() && !mainJson.getString("muvi_uniq_id").trim().equals("null") && !mainJson.getString("muvi_uniq_id").trim().matches("")) {
                        contentDetailsOutput.setMuviUniqId(mainJson.getString("muvi_uniq_id"));
                    } else {
                        contentDetailsOutput.setMuviUniqId("");

                    }

                    if ((mainJson.has("movieUrl")) && mainJson.getString("movieUrl").trim() != null && !mainJson.getString("movieUrl").trim().isEmpty() && !mainJson.getString("movieUrl").trim().equals("null") && !mainJson.getString("movieUrl").trim().matches("")) {
                        contentDetailsOutput.setMovieUrl(mainJson.getString("movieUrl"));

                    } else {
                        contentDetailsOutput.setMovieUrl("");

                    }

                    if ((mainJson.has("banner")) && mainJson.getString("banner").trim() != null && !mainJson.getString("banner").trim().isEmpty() && !mainJson.getString("banner").trim().equals("null") && !mainJson.getString("banner").trim().matches("")) {
                        contentDetailsOutput.setBanner(mainJson.getString("banner"));
                    } else {
                        contentDetailsOutput.setBanner("");

                    }


                    if ((mainJson.has("poster")) && mainJson.getString("poster").trim() != null && !mainJson.getString("poster").trim().isEmpty() && !mainJson.getString("poster").trim().equals("null") && !mainJson.getString("poster").trim().matches("")) {
                        contentDetailsOutput.setPoster(mainJson.getString("poster"));
                    } else {
                        contentDetailsOutput.setPoster("");

                    }


                    if (mainJson.has("cast_detail")) {
                        JSONArray castArray = mainJson.getJSONArray("cast_detail");
                        Log.v("SUBHA", "cast_detail" + castArray.length());


                        StringBuilder sb = new StringBuilder();
                        if (castArray.length() > 0) {
                            for (int i = 0; i < castArray.length(); i++) {
                                JSONObject jsonChildNode = castArray.getJSONObject(i);
                                if (jsonChildNode.has("celeb_name")){
                                    sb.append(jsonChildNode.getString("celeb_name").toString());
                                    if (i != castArray.length() - 1) {
                                        sb.append(",");
                                    }

                                }

                            }
                            contentDetailsOutput.setArtist(sb.toString());
                            Log.v("SUBHA","SB"+sb.toString());

                        }else{

                        }
                    } else {


                    }





                    if ((mainJson.has("isFreeContent")) && mainJson.getString("isFreeContent").trim() != null && !mainJson.getString("isFreeContent").trim().isEmpty() && !mainJson.getString("isFreeContent").trim().equals("null") && !mainJson.getString("isFreeContent").trim().matches("")) {
                        contentDetailsOutput.setIsFreeContent(mainJson.getString("isFreeContent"));
                    } else {
                        contentDetailsOutput.setIsFreeContent(mainJson.getString("isFreeContent"));

                    }
                    if ((mainJson.has("release_date")) && mainJson.getString("release_date").trim() != null && !mainJson.getString("release_date").trim().isEmpty() && !mainJson.getString("release_date").trim().equals("null") && !mainJson.getString("release_date").trim().matches("")) {
                        contentDetailsOutput.setReleaseDate(mainJson.getString("release_date"));
                    } else {
                        contentDetailsOutput.setReleaseDate(mainJson.getString("isFreeContent"));

                    }
                    if ((mainJson.has("is_ppv")) && mainJson.getString("is_ppv").trim() != null && !mainJson.getString("is_ppv").trim().isEmpty() && !mainJson.getString("is_ppv").trim().equals("null") && !mainJson.getString("is_ppv").trim().matches("")) {
                        contentDetailsOutput.setIsPpv(Integer.parseInt(mainJson.getString("is_ppv")));
                    } else {
                        contentDetailsOutput.setIsPpv(0);

                    }
                    if ((mainJson.has("is_converted")) && mainJson.getString("is_converted").trim() != null && !mainJson.getString("is_converted").trim().isEmpty() && !mainJson.getString("is_converted").trim().equals("null") && !mainJson.getString("is_converted").trim().matches("")) {
                        contentDetailsOutput.setIsConverted(Integer.parseInt(mainJson.getString("is_converted")));
                    } else {
                        contentDetailsOutput.setIsConverted(0);

                    }
                    if ((mainJson.has("is_advance")) && mainJson.getString("is_advance").trim() != null && !mainJson.getString("is_advance").trim().isEmpty() && !mainJson.getString("is_advance").trim().equals("null") && !mainJson.getString("is_advance").trim().matches("")) {
                        contentDetailsOutput.setIsApv(Integer.parseInt(mainJson.getString("is_advance")));
                    } else {
                        contentDetailsOutput.setIsApv(0);

                    }
                    if (contentDetailsOutput.getIsPpv() == 1) {
                        JSONObject ppvJson = null;
                        if ((myJson.has("ppv_pricing"))) {

                            PPVModel ppvModel = new PPVModel();
                            ppvJson = myJson.getJSONObject("ppv_pricing");
                            if ((ppvJson.has("price_for_unsubscribed")) && ppvJson.getString("price_for_unsubscribed").trim() != null && !ppvJson.getString("price_for_unsubscribed").trim().isEmpty() && !ppvJson.getString("price_for_unsubscribed").trim().equals("null") && !ppvJson.getString("price_for_unsubscribed").trim().matches("")) {
                                ppvModel.setPPVPriceForUnsubscribedStr(ppvJson.getString("price_for_unsubscribed"));
                            } else {
                                ppvModel.setPPVPriceForUnsubscribedStr("0.0");

                            }
                            if ((ppvJson.has("price_for_subscribed")) && ppvJson.getString("price_for_subscribed").trim() != null && !ppvJson.getString("price_for_subscribed").trim().isEmpty() && !ppvJson.getString("price_for_subscribed").trim().equals("null") && !ppvJson.getString("price_for_subscribed").trim().matches("")) {
                                ppvModel.setPPVPriceForsubscribedStr(ppvJson.getString("price_for_subscribed"));
                            } else {
                                ppvModel.setPPVPriceForsubscribedStr("0.0");

                            }
                            contentDetailsOutput.setPpvDetails(ppvModel);
                        }

                    }
                    if (contentDetailsOutput.getIsApv() == 1) {
                        JSONObject advJson = null;
                        if ((myJson.has("adv_pricing"))) {
                            APVModel aPVModel = new APVModel();

                            advJson = myJson.getJSONObject("adv_pricing");
                            if ((advJson.has("price_for_unsubscribed")) && advJson.getString("price_for_unsubscribed").trim() != null && !advJson.getString("price_for_unsubscribed").trim().isEmpty() && !advJson.getString("price_for_unsubscribed").trim().equals("null") && !advJson.getString("price_for_unsubscribed").trim().matches("")) {
                                aPVModel.setAPVPriceForUnsubscribedStr(advJson.getString("price_for_unsubscribed"));

                            } else {
                                aPVModel.setAPVPriceForUnsubscribedStr("0.0");

                            }
                            if ((advJson.has("price_for_subscribed")) && advJson.getString("price_for_subscribed").trim() != null && !advJson.getString("price_for_subscribed").trim().isEmpty() && !advJson.getString("price_for_subscribed").trim().equals("null") && !advJson.getString("price_for_subscribed").trim().matches("")) {
                                aPVModel.setAPVPriceForsubscribedStr(advJson.getString("price_for_subscribed"));
                            } else {
                                aPVModel.setAPVPriceForsubscribedStr("0.0");

                            }
                            contentDetailsOutput.setApvDetails(aPVModel);

                        }

                    }

                    if (contentDetailsOutput.getIsPpv() == 1 || contentDetailsOutput.getIsApv() == 1) {

                        JSONObject currencyJson = null;
                        if (myJson.has("currency") && myJson.getString("currency") != null && !myJson.getString("currency").equals("null")) {
                            currencyJson = myJson.getJSONObject("currency");
                            CurrencyModel currencyModel = new CurrencyModel();

                            if (currencyJson.has("id") && currencyJson.getString("id").trim() != null && !currencyJson.getString("id").trim().isEmpty() && !currencyJson.getString("id").trim().equals("null") && !currencyJson.getString("id").trim().matches("")) {
                                currencyModel.setCurrencyId(currencyJson.getString("id"));
                            } else {
                                currencyModel.setCurrencyId("");

                            }
                            if (currencyJson.has("country_code") && currencyJson.getString("country_code").trim() != null && !currencyJson.getString("country_code").trim().isEmpty() && !currencyJson.getString("country_code").trim().equals("null") && !currencyJson.getString("country_code").trim().matches("")) {
                                currencyModel.setCurrencyCode(currencyJson.getString("country_code"));
                            } else {
                                currencyModel.setCurrencyCode("");
                            }
                            if (currencyJson.has("symbol") && currencyJson.getString("symbol").trim() != null && !currencyJson.getString("symbol").trim().isEmpty() && !currencyJson.getString("symbol").trim().equals("null") && !currencyJson.getString("symbol").trim().matches("")) {
                                currencyModel.setCurrencySymbol(currencyJson.getString("symbol"));
                            } else {
                                currencyModel.setCurrencySymbol("");
                            }
                            contentDetailsOutput.setCurrencyDetails(currencyModel);

                        }

                    }




                       /* if (mainJson.has("cast_detail") && mainJson.has("cast_detail")!= false && mainJson.getString("cast_detail").trim() != null && !mainJson.getString("cast_detail").trim().isEmpty() && !mainJson.getString("cast_detail").trim().equals("null") && !mainJson.getString("cast_detail").trim().equals("false")){
                            JSONArray castDetailArray = mainJson.getJSONArray("cast_detail");
                            int lengthJsonArr = castDetailArray.length();
                            for(int i=0; i < lengthJsonArr; i++) {
                                JSONObject jsonChildNode;
                                try {
                                    jsonChildNode = castDetailArray.getJSONObject(i);
                                    if (jsonChildNode.has("cast_type") && jsonChildNode.getString("cast_type").equalsIgnoreCase("actor")){
                                        tempStr.append( jsonChildNode.getString("celeb_name")+"\n");
                                        castStr = tempStr.toString();

                                    }else  if (jsonChildNode.has("cast_type") && jsonChildNode.getString("cast_type").equalsIgnoreCase("director")){
                                        crewtempStr.append( jsonChildNode.getString("celeb_name")+"\n");
                                        crewStr = crewtempStr.toString();

                                    }

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                        }else{
                            castStr = "";
                            crewStr = "";
                        }
*/
                }
            } else {

                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            status = 0;
            message = "Error";
        } catch (Exception e) {

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetContentDetailsPreExecuteStarted();

        status = 0;
      /*  if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetContentDetailsPostExecuteCompleted(contentDetailsOutput, status, message);
            return;
        }
        if (CommonConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetContentDetailsPostExecuteCompleted(contentDetailsOutput, status, message);
        }*/


    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGetContentDetailsPostExecuteCompleted(contentDetailsOutput, status, message);

    }

    //}
}
