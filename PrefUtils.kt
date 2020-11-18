import android.content.Context
import java.io.File
import java.util.*

/**
 * <pre>
 * {Util - Preferences - Migration}.
 * </pre>
 * <caustion>% 기존의 Preferences 의 파일 추적이 어려워서 내부 패키지에서 파일 탐색 후 migrate.</caution>
 *
 *
 * @author shwoo
 * @since 2020.11.10
 *
 *
 * @history 2020.11.10 :: shwoo :: seperate module class.
 */
class PrefUtils {
    companion object {
        private val TAG = PrefUtils::class.simpleName

        /** agreement states of Push Notification */
        private const val KEY_PUSH_AGREEMENT_STATE = "pushAgreementState"
        /** name of Preference :: Google Cloud message(GCM) */
        private const val PREF_NAME_GCM = "pref_notification"
    }



    private var ctx: Context? = null
    fun init(ctx: Context?) {
        this.ctx = ctx
    }

    /**
     * <pre>
     * [Preference] migrate old preference to new preference.
     * </pre>
     */
    fun migrateOldPref() {
        val f = ctx!!.cacheDir
        val isDirectory = f != null && f.isDirectory
        if (isDirectory) {
            // change to parent. (/data/user/0/com.interpark.tour.mobile.main.dev/)
            val fParent = File(f!!.parent)
            val fPrefList =
                    getSharedPrefFiles(ctx, fParent)
            if (!fPrefList.isEmpty()) {
                val descending = PrefUtil.FileSort()
                Collections.sort(fPrefList, descending)

                // TODO :: 2019.11.11 shwoo :: please check this logic correct ???
                val fPref = fPrefList[0]
                val recentPrefName = fPref.name.replace(".xml", "")

                // 최종 파일이 없으면,
                if (recentPrefName != PREF_NAME_GCM) {
                    insertToNewSharedPrefFile(ctx, fPref)
                }
            }
        }
    }


    /**
     * <pre>
     * [ShreadPreference] :: get Preference files contain 'pushAgreementState' key.
     * </pre>
     *
     * @param ctx     current context.
     * @param fParent parent directory.
     * @return preferences list.
     */
    private fun getSharedPrefFiles(
            ctx: Context?,
            fParent: File
    ): ArrayList<File> {
        val fPrefList = ArrayList<File>()
        for (fDirectory in fParent.listFiles()) {
            if ("shared_prefs".equals(fDirectory.name, ignoreCase = true)) {
                // fDirectory = /data/user/0/com.interpark.tour.mobile.main.dev/shared_prefs
                for (fTemp in fDirectory.listFiles()) {
                    var name = fTemp.name
                    name = name.replace(".xml", "")
                    val pref =
                            ctx!!.getSharedPreferences(name, Context.MODE_PRIVATE)
                    if (pref.contains(KEY_PUSH_AGREEMENT_STATE)) {
                        fPrefList.add(fTemp)
                    }
                }
                break
            }
        }
        return fPrefList
    }



    /**
     * <pre>
     * [Migration] new shared preferences.
     * </pre>
     *
     * @param ctx
     * @param fPref previous version file.
     */
    private fun insertToNewSharedPrefFile(
            ctx: Context?,
            fPref: File
    ) {
        val fNew =
                File(fPref.parent + File.separator + PREF_NAME_GCM + ".xml")
        val isSuccess = fPref.renameTo(fNew)
        if (isSuccess) {
            Logger.w(
                    TAG,
                    "is Rename Success [" + fPref.path + "] -> [" + fNew.path + "]"
            )
            ////////////////////////////////////////////////////
            // logging.
            // 2019.11.11 shwoo :: loggin in debug.
            if (BuildConfig.DEBUG) {
                val pref = ctx!!.getSharedPreferences(
                        PREF_NAME_GCM,
                        Context.MODE_PRIVATE
                )
                val map: Map<*, *> = pref.all
                val sb = StringBuilder(map.size)
                for (key in map.keys) {
                    sb.append("\n")
                    sb.append("[" + key + "] = [" + map[key] + "]")
                }
                Logger.w(
                        PREF_NAME_GCM,
                        sb.toString()
                )
            }
            ////////////////////////////////////////////////////
        }
    }
}