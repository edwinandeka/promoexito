package com.ekasoft.promoexito.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

/**
 * @class RequestHttp
 * @author edwin.ospina@iptotal.com( Edwin Ramiro Ospina) -
 *         juan.bernal@iptotal.com( Juan Martín Bernal)
 * @date 11 de diciembre 2014
 *
 * 
 * @description Clase encargada de hacer peticiones http, por sus diferentes
 *              metodos (GET,POST,DELETE)
 */
public class RequestHttp {

	public static final int RESQUEST_DELETE = 0;
	public static final int RESQUEST_GET = 1;
	public static final int RESQUEST_POST = 2;

	private List<BasicNameValuePair> params;
	private HttpPost httppost;
	private HttpDelete httpDelete;
	private HttpGet httpGet;
	private int type = 0;
	private String webservice = "";
	private int timeOut = 0;

	/*
	 * constructor
	 */
	public RequestHttp(String webservice, int type) {

		this.type = type;

		params = new ArrayList<BasicNameValuePair>();
		if (type == RESQUEST_POST)
			httppost = new HttpPost(webservice);
		else
			webservice = webservice + "?";

		httpGet = new HttpGet(webservice);

		this.webservice = webservice;

	}

	/*
	 * Metodo que define el tiempo de espera de la petición
	 */
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * envia la peticion al servidor
	 * 
	 * @return la respuesta del servidor
	 */
	public String send() {
	if (type== RESQUEST_GET)
		webservice = webservice.substring(0,webservice.length()-1);

		HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters,
				(timeOut != 0) ? timeOut : 60000);
		HttpConnectionParams.setSoTimeout(httpParameters,
				(timeOut != 0) ? timeOut : 60000);
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpResponse httpResponse = null;
		try {
			switch (type) {
			case RESQUEST_DELETE:
				httpDelete = new HttpDelete(webservice);
				httpResponse = httpclient.execute(httpDelete);
				break;

			case RESQUEST_GET:

				httpResponse = httpclient.execute(httpGet);
				break;
			case RESQUEST_POST:
				httppost.setEntity(new UrlEncodedFormEntity(params));
				httpResponse = httpclient.execute(httppost);
				break;

			default:
				break;
			}

			InputStream inputStream = httpResponse.getEntity().getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);

			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();

			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}
			Log.d("RequestHttp", "nombre del servicio: "+webservice);
			Log.d("RequestHttp", stringBuilder.toString());
			return stringBuilder.toString();

		} catch (ClientProtocolException e) {

			return null;
		} catch (IOException e) {

			return null;
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}

	}

	/**
	 * Metodo encargado de agregar los parametros a la petición.
	 * 
	 * @param key
	 *            llave del parametro que se envia
	 * @param value
	 *            valor que lleva el parametro en la petición
	 */
	public void addParam(String key, String value) {
		Log.d("RequestHttp", key + "=>" + value);
		
		if (type == RESQUEST_POST)
			params.add(new BasicNameValuePair(key, value));
		else
			webservice = webservice + key + "=" + value + "&";

	}

	/**
	 * Metodo que establece la cabecera de la petición
	 *
	 * @param key
	 * @param value
	 */
	public void setHeader(String key, String value) {
		if (type == RESQUEST_POST)
			httppost.setHeader(key, value);
		if (type == RESQUEST_GET)
		httpGet.setHeader(key, value);
	}

}
