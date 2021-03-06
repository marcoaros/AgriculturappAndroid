package com.interedes.agriculturappv3.services.sms

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns
import android.provider.ContactsContract
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.telephony.SmsMessage
import com.interedes.agriculturappv3.R
import com.interedes.agriculturappv3.activities.chat.chat_sms.detail_sms_user.Chat_Sms_Activity
import com.interedes.agriculturappv3.activities.chat.chat_sms.detail_sms_user.events.RequestEventSmsDetail
import com.interedes.agriculturappv3.activities.chat.chat_sms.user_sms_ui.SettingsActivity
import com.interedes.agriculturappv3.activities.chat.chat_sms.user_sms_ui.events.RequestEventUserSms
import com.interedes.agriculturappv3.libs.EventBus
import com.interedes.agriculturappv3.libs.GreenRobotEventBus
import com.interedes.agriculturappv3.modules.main_menu.ui.events.RequestEventMainMenu
import com.interedes.agriculturappv3.modules.models.sms.Sms
import com.interedes.agriculturappv3.modules.notification.events.RequestEventsNotification
import com.interedes.agriculturappv3.services.resources.EmisorType_Message_Resources
import com.interedes.agriculturappv3.services.resources.MessageSmsType
import com.interedes.agriculturappv3.services.resources.NotificationTypeResources
import com.interedes.agriculturappv3.services.resources.TagSmsResources
import com.interedes.agriculturappv3.services.sms.repository.IMainViewSms
import com.interedes.agriculturappv3.services.sms.repository.NotificationSmsRepository
import java.util.*


class MySmsBroadcastReceiver: BroadcastReceiver() {

    val SMS_BUNDLE = "pdus"
    var mChannel: NotificationChannel? = null
    var notifManager: NotificationManager? = null
    private val ADMIN_CHANNEL_ID = "admin_channel"
    var repository: IMainViewSms.Repository? = null
    var eventBus: EventBus? = null
    init {
        eventBus = GreenRobotEventBus()
        repository = NotificationSmsRepository()
    }

    /**
     * onReceive
     * Purpose:
     * Listens for SMS messages and adds them to the main List.
     * @param context
     * @param intent
     */
    override fun onReceive(context: Context, intent: Intent) {
        val intentExtras = intent.extras
        if (intentExtras != null) {
            val sms = intentExtras.get(SMS_BUNDLE) as Array<Any>
            var smsMessageStr = ""
            var smsBody = ""
            var smsAddress = ""
            for (i in sms.indices) {
                val smsMessage = SmsMessage.createFromPdu(sms[i] as ByteArray)

                smsBody = smsMessage.getMessageBody()
                smsAddress = smsMessage.getOriginatingAddress()

                smsMessageStr += "$smsAddress\n"
                smsMessageStr += smsBody + "\n"
            }

            if(smsMessageStr.contains(context.getString(R.string.idenfication_sms_app))){


                //Get the user's settings
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                //Get the value for notifications
                val notificationsOn = sharedPreferences.getBoolean(SettingsActivity.NOTIFICATIONS_KEY, true)
                //boolean vibratorOn = sharedPreferences.getBoolean(SettingsActivity.VIBRATION_KEY, true);

                var messageContent=""
                if (notificationsOn) {
                    messageContent= smsBody.replace(context.getString(R.string.idenfication_send_sms_app),"")
                    var messageAdress=getContactDisplayNameByNumber(smsAddress,context)
                    if(messageAdress.equals("")){
                        messageAdress=smsAddress
                    }
                    postEventUserSmsActivity(RequestEventUserSms.NEW_MESSAGE_EVENT,null,null,smsMessageStr)
                    postEventChatSmsActivity(RequestEventSmsDetail.NEW_MESSAGE_EVENT,null,null,smsMessageStr)

                    //Build the notification:

                    displayCustomNotificationForOrders(messageAdress, messageContent, context,smsAddress,messageAdress)
                }

                if (!messageContent.contains(context.getString(R.string.idenfication_send_sms_oferta_app))) {

                    val fcmNotificationBuilder= com.interedes.agriculturappv3.modules.models.Notification.NotificationLocal()
                    fcmNotificationBuilder.title= NotificationTypeResources.NOTIFICATION_TYPE_OFERTA
                    fcmNotificationBuilder.message= messageContent
                    fcmNotificationBuilder.user_name=""
                    fcmNotificationBuilder.ui=""
                    fcmNotificationBuilder.fcm_token=""
                    fcmNotificationBuilder.room_id=""
                    fcmNotificationBuilder.type_notification= NotificationTypeResources.NOTIFICATION_TYPE_OFERTA
                    fcmNotificationBuilder.image_url= ""
                    fcmNotificationBuilder.parameter=""
                    repository?.saveNotification(fcmNotificationBuilder)

                    postEventMenu(RequestEventMainMenu.UPDATE_BADGE_NOTIIFCATIONS,null,null,null)
                    postEventNotifications(RequestEventsNotification.RELOAD_LIST_NOTIFICATION,null,null,null)
                }



            }
        }
    }

    fun getContactDisplayNameByNumber(number: String?, context:Context?): String {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number))
        var name = ""
        val contentResolver = context?.contentResolver
        val contactLookup = contentResolver?.query(uri, arrayOf(BaseColumns._ID, ContactsContract.PhoneLookup.DISPLAY_NAME), null, null, null)
        try {
            if (contactLookup != null && contactLookup.count > 0) {
                contactLookup.moveToNext()
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }
        } finally {
            contactLookup?.close()
        }

        return name
    }


    private fun displayCustomNotificationForOrders(title: String, description: String, context: Context,smsAddress:String,messageAdress:String) {
        if (notifManager == null) {
            notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        val notificationId = Random().nextInt(60000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val builder: NotificationCompat.Builder
            val intent = Intent(context, Chat_Sms_Activity::class.java)


            val sms= Sms(0,
                    "",
                    smsAddress,
                    "",
                    "",
                    System.currentTimeMillis().toString(),
                    MessageSmsType.MESSAGE_TYPE_SENT,
                    EmisorType_Message_Resources.MESSAGE_EMISOR_TYPE_SMS,
                    messageAdress,
                    selectedItemList = false
            )

            intent.putExtra(TagSmsResources.TAG_SMS,sms)
            
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent: PendingIntent
            setupChannels(context)
            /*
            val importance = NotificationManager.IMPORTANCE_HIGH
            if (mChannel == null) {
                mChannel = NotificationChannel("0", title, importance)
                mChannel?.setDescription(description)
                mChannel?.enableVibration(true)
                notifManager?.createNotificationChannel(mChannel)
            }*/
            builder = NotificationCompat.Builder(context, ADMIN_CHANNEL_ID)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            pendingIntent = PendingIntent.getActivity(context, notificationId+1, intent, PendingIntent.FLAG_ONE_SHOT)
            builder.setContentTitle(title)
                    .setSmallIcon(getNotificationIcon()) // required
                    .setContentText(description)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setStyle( NotificationCompat.BigTextStyle()
                            .bigText(description))
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_notification))
                    //.setGroup(smsAddress)
                    .setBadgeIconType(R.mipmap.ic_launcher_notification)
                    .setContentIntent(pendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setShowWhen(true)
                    .setWhen(Calendar.getInstance().getTimeInMillis())
            val notification = builder.build()
            notification.defaults=(Notification.DEFAULT_SOUND)
            notification.defaults=(Notification.DEFAULT_VIBRATE)
            notifManager?.notify(notificationId, notification)
        } else {

            val intent = Intent(context, Chat_Sms_Activity::class.java)

            intent.putExtra(TagSmsResources.PHONE_NUMBER,smsAddress)
            intent.putExtra(TagSmsResources.CONTACT_NAME,messageAdress)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            var pendingIntent: PendingIntent? = null

            pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_ONE_SHOT)

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setAutoCancel(true)
                    ///.setColor(context.getColor(R.color.colorPrimary))
                    .setSound(defaultSoundUri)
                    .setSmallIcon(getNotificationIcon())
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(description))

            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND)
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE)

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.mipmap.ic_launcher else R.mipmap.ic_launcher
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(context:Context) {
        val adminChannelName = context.getString(R.string.notifications_admin_channel_name)
        val adminChannelDescription = context.getString(R.string.notifications_admin_channel_description)

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        if (notifManager != null) {
            notifManager!!.createNotificationChannel(adminChannel)
        }
    }


    //region EVETNS
    private fun postEventChatSmsActivity(type: Int, listModel1:MutableList<Object>?,model:Object?,errorMessage: String?) {
        val event = RequestEventSmsDetail(type, listModel1, model, errorMessage)
        event.eventType = type
        eventBus?.post(event)
    }
    private fun postEventUserSmsActivity(type: Int, listModel1:MutableList<Object>?,model:Object?,errorMessage: String?) {
        val event = RequestEventUserSms(type, listModel1, model, errorMessage)
        event.eventType = type
        eventBus?.post(event)
    }


    private fun postEventNotifications(type: Int, listModel1:MutableList<Object>?,model:Object?,errorMessage: String?) {
        val event = RequestEventsNotification(type, listModel1, model, errorMessage)
        event.eventType = type
        eventBus?.post(event)
    }
    private fun postEventMenu(type: Int, listModel1:MutableList<Object>?,model:Object?,errorMessage: String?) {
        val event = RequestEventMainMenu(type, listModel1, model, errorMessage)
        event.eventType = type
        eventBus?.post(event)
    }

    //end region


}