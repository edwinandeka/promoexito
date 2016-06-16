/*
 *  @class: NotificationIPtotal	
 *  @author: edwin.ospina@iptotal.com( Edwin Ramiro Ospina) - juan.bernal@iptotal.com( Juan Mart�n Bernal)
 *  @date: 11 de marzo 2014
 *  
 *  @version: 1.0
 *  
 *  @Licence: Derechos reservados de Autor (c) IP Total Software S.A.
 */

package com.ekasoft.promoexito.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ekasoft.promoexito.R;

public class NotificationUtil {
	private static NotificationManager notificationManager;

	/**
	 * 
	 * @param context
	 * @param title
	 * @param text
	 * @param info
	 * @param ticker
	 * @param activity
	 */
	public static void create(Context context, String title, String text,
			String info, String ticker, int largeIcon, int smallIcon,
			Class<?> activity, Uri soundUri) {
		try {

			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					context);

			builder.setSmallIcon(smallIcon)
					.setLargeIcon(
							BitmapFactory.decodeResource(
									context.getResources(), largeIcon))
					.setContentTitle(title).setContentText(text)
					.setContentInfo(info).setTicker(ticker)
					.setLights(0xFFFF0000, 500, 500).setAutoCancel(true)
					.setSound(soundUri);

			if (activity != null) {
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, new Intent(context, activity), 0);
				builder.setContentIntent(pendingIntent);
			}

			notificationManager.notify(largeIcon, builder.build());

		} catch (Exception e) {
			Log.d("Exception: ", e.getMessage());
		}
	}

	public static void doNotify(Context context, Class activity, String messsage) {
		/*SharedPreferences mySharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean my_checkbox_preference = mySharedPreferences.getBoolean(
				"notificacion", true);

		boolean launch_notification = mySharedPreferences.getBoolean(
				"launch_notificacion", true);
		if (launch_notification) {*/
			Uri sound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			NotificationCompat.Builder nb = new NotificationCompat.Builder(
					context);
			nb.setSmallIcon(R.drawable.icono);

			//if (my_checkbox_preference)
				nb.setSound(sound);

			nb.setAutoCancel(true);
			nb.setContentTitle("ssociall");
			nb.setContentText(messsage);

			
			nb.setContentIntent(PendingIntent.getActivity(context, 100,
					new Intent(context, activity), 0));

			NotificationManager nm = (NotificationManager) context
					.getSystemService(context.NOTIFICATION_SERVICE);
			nm.notify(100, nb.build());

	}

}
