import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 * [DateUtil] :: Date Manager class.
</pre> *
 *
 * @author shwoo
 * @history shwoo | 2019.09.17 | create.
 * @since 2019.09.17
 */
class DateUtil private constructor() {
    val DAYS = arrayOf("일", "월", "화", "수", "목", "금", "토")
    private val MILLS_ONE_DAY = 24 * 60 * 60 * 1000

    object DateFormat {
        const val DIVIDER = "-"
        const val SUFFIX_DAY = "일"
        const val SUFFIX_MONTH = "월"
        const val SUFFIX_YEAR = "년"
        const val YYYY = "yyyy"

        /**
         * DateFormat :: yyyy년
         */
        const val YYYY_S = YYYY + SUFFIX_YEAR

        /**
         * DateFormat :: MM월
         */
        const val MM = "MM$SUFFIX_MONTH"

        /**
         * DateFormat :: dd일
         */
        const val DD = "dd$SUFFIX_DAY"

        /**
         * DateFormat :: MM월dd일
         */
        const val MM_DD = MM + DD

        /**
         * DateFormat :: MM월dd일 수
         */
        const val MM_DD_E = "$MM_DD E"

        /**
         * DateFormat ::yyyy년 MM월dd일 수
         */
        const val YEAR_MM_DD_E = "$YYYY_S $MM_DD_E"

        /**
         * DateFormat :: yyyy년 MM월dd일 HH시
         */
        const val YEAR_MONTH_DT = "$YYYY_S $MM_DD HH시"

        /**
         * DateFormat :: yyyy-MM-dd
         */
        const val YEAR_MONTH_DAY = "$YYYY-MM-dd"

        /**
         * DateFormat :: yyyyMMdd
         */
        const val YEAR_MONTH_DATE_PURE = YYYY + "MMdd"

        /**
         * DateFormat :: yyyyMMddHHmmss
         */
        const val YEAR_MONTH_DATE_TIMEMILIS = YEAR_MONTH_DATE_PURE + "HHmmss"

        /**
         * DateFormat :: yyyy-MM-dd HH:mm:ss
         */
        const val YEAR_MONTH_DAY_HOURS = "$YYYY-MM-dd HH:mm:ss"

        /**
         * DateFormat :: yyyy-MM
         */
        const val YEAR_MONTH = YYYY + DIVIDER + "MM"

        /**
         * DateFormat :: yyyy.MM.dd
         */
        const val YEAR_MONTH_DATE = "$YYYY.MM.dd"

        /**
         * Convert Format :: @Link{DateUtil#YEAR_MONTH_DATE} yyyy.MM.dd <-> %04d.%02d.%02d
         */
        const val STR_YEAR_MONTH_DATE = "%04d.%02d.%02d"
        const val MONTH_DATE = "MM.dd"
        const val HH_MM = "HH시간 mm분"
    }

    fun isEqualDate(d1: Date?, d2: Date?): Boolean {
        val strD1Date = getCurrentYearMonthDateDotStr(d1)
        val strD2Date = getCurrentYearMonthDateDotStr(d2)
        return strD1Date == strD2Date
    }

    /**
     * <pre>
     * 이전 날짜 인지 비교.
    </pre> *
     *
     * @param d1
     * @param d2
     * @return
     */
    fun isPreviousDate(d1: Date?, d2: Date?): Boolean {
        val sdf = getDateFormat(DateFormat.YEAR_MONTH_DAY)
        val sD1 = sdf.format(d1)
        val sD2 = sdf.format(d2)
        return sD1.compareTo(sD2) < 0
    }

    @Throws(NullPointerException::class)
    private fun getDateFormat(originalFormat: String): SimpleDateFormat {
        return SimpleDateFormat(originalFormat, Locale.KOREA)
    }

    val calendar: Calendar
        get() = Calendar.getInstance()

    /**
     * <pre>
     * [Convert] Date to dateString.
    </pre> *
     */
    private fun formatToString(format: String, d: Date?): String {
        val sdf = getDateFormat(format)
        return sdf.format(d)
    }

    /**
     * <pre>
     * [Parse] dateString to Date.
    </pre> *
     */
    @Throws(ParseException::class)
    private fun parseToDate(format: String, date: String): Date {
        val sdf = getDateFormat(format)
        return sdf.parse(date)
    }

    /**
     * <pre>
     * [Parse] dateString to Date ( yyyy-MM-dd )
    </pre> *
     *
     * @param dateStr 'yyyy-MM-dd' format string.
     * @return
     */
    fun parseToDate(dateStr: String?): Date {
        val sdf = getDateFormat(DateFormat.YEAR_MONTH_DAY)
        try {
            return sdf.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        // default :: now time.
        return calendar.time
    }

    /**
     * <pre>
     * [Convert] dateString to Converted dateString.
    </pre> *
     *
     * @param date           original dateString.
     * @param originalFormat original format.
     * @param convertFormat  convert format.
     */
    @Throws(ParseException::class)
    fun getConvertedFormatStr(date: String, originalFormat: String, convertFormat: String): String {
        val d = parseToDate(originalFormat, date)
        return formatToString(convertFormat, d)
    }

    fun getFormatString(date: Date?, format: String?, locale: Locale?): String {
        var locale = locale
        if (locale == null) {
            locale = Locale.KOREA
        }
        val sdf = SimpleDateFormat(format, locale)
        return sdf.format(date)
    }

    /**
     * <pre>
     * [Convert] timestamp to current format dateString.
    </pre> *
     *
     * @param timestamp time stamp.
     * @param format    날짜형식
     * @return format date
     * @author SJ JANG
     * @description long 타입으로 받아온 timestamp를 format에 맞게 리턴
     */
    @Throws(NullPointerException::class)
    fun getTimestampToStringFormat(timestamp: Long, format: String): String {
        return getDateFormat(format).format(timestamp)
    }

    /**
     * <pre>
     * [Convert] dateString ( [DateFormat.YEAR_MONTH_DAY] ) to Calendar.
    </pre> *
     *
     * @return set time Calendar instance.
     */
    @Throws(ParseException::class)
    fun getCalendarTargetDate(dateString: String?): Calendar {
        val date: java.text.DateFormat = getDateFormat(DateFormat.YEAR_MONTH_DAY)
        val c = calendar
        c.time = date.parse(dateString)
        return c
    }

    /**
     * <pre>
     * [Convert] Date -> Calendar
    </pre> *
     *
     * @param date
     * @return
     */
    fun getCalendarByDate(date: Date?): Calendar {
        val c = calendar
        c.time = date
        return c
    }

    /**
     * <pre>
     * [Convert] Date -> Calendar
    </pre> *
     *
     * @param date
     * @return
     */
    fun getCalendarListByAddDays(date: Date, addDays: Int): ArrayList<Calendar> {
        return genCalendarListByAddField(date, addDays, Calendar.DATE)
    }

    /**
     * <pre>
     * [Convert] Date -> Calendar
    </pre> *
     *
     * @param date
     * @return
     */
    fun getCalendarListByAddYears(date: Date, addYears: Int): ArrayList<Calendar> {
        return genCalendarListByAddField(date, addYears, Calendar.YEAR)
    }

    private fun genCalendarListByAddField(date: Date, addYears: Int, field: Int): ArrayList<Calendar> {
        val arrList = ArrayList<Calendar>()
        val cBegin = calendar
        cBegin.time = date
        val cEnd = calendar
        cEnd.time = date
        cEnd.add(field, addYears)
        var cTemp: Calendar? = null
        while (cBegin.compareTo(cEnd) != 1) {
            cTemp = calendar
            cTemp.time = cBegin.time
            arrList.add(cTemp)
            cBegin.add(field, 1)
        }
        return arrList
    }

    /**
     * <pre>
     * [Convert] current time to dateString. ( [DateFormat.YEAR_MONTH_DATE_TIMEMILIS] )
    </pre> *
     *
     * @return current formatted dateString.
     */
    val timeStampString: String
        get() = formatToString(DateFormat.YEAR_MONTH_DATE_TIMEMILIS, currentDate)

    /**
     * <pre>
     * [Convert] current time to dateString. ( [DateFormat.YEAR_MONTH_DATE_TIMEMILIS] )
    </pre> *
     *
     * @return current formatted dateString.
     */
    val currentDateYYYYMMDD: String
        get() = formatToString(DateFormat.YEAR_MONTH_DATE_PURE, currentDate)

    val currentDate: Date
        get() = Date()

    /**
     * <pre>
     * [Date] add years.
    </pre> *
     *
     * @param amount
     * @return
     */
    fun getAddYears(amount: Int): Date {
        val cal = calendar
        cal.add(Calendar.YEAR, amount)
        return cal.time
    }

    /**
     * <pre>
     * []
    </pre> *
     *
     * @param amount
     * @return
     */
    fun getAddDays(amount: Int): Date {
        val cal = calendar
        cal.add(Calendar.DATE, amount)
        return cal.time
    }

    /**
     * <pre>
     * []
    </pre> *
     *
     * @param amount
     * @return
     */
    fun getAddDays(date: Date?, amount: Int): Date {
        val cal = calendar
        cal.time = date
        cal.add(Calendar.DATE, amount)
        return cal.time
    }

    /**
     * <pre>
     * []
    </pre> *
     *
     * @param amount
     * @return
     */
    fun getAddMonth(amount: Int): Date {
        val cal = calendar
        cal.add(Calendar.MONTH, amount)
        return cal.time
    }

    val currentTime: Long
        get() = currentDate.time

    /**
     * <pre>
     * [Convert] current time to dateString. ( [DateFormat.YEAR_MONTH_DT] )
    </pre> *
     *
     * @return current formatted dateString.
     */
    val currentTimeStr: String
        get() = formatToString(DateFormat.YEAR_MONTH_DT, currentDate)

    /**
     * <pre>
     * [Convert] current time to dateString. ( [DateFormat.YEAR_MONTH] )
    </pre> *
     *
     * @return current formatted dateString.
     */
    val currentMonthStr: String
        get() = formatToString(DateFormat.YEAR_MONTH, currentDate)

    /**
     * <pre>
     * [Convert] current time to dataString ( [DateFormat.YEAR_MONTH_DAY_HOURS])
    </pre> *
     *
     * @return current formatted dataString.
     */
    val currentTimeSecondStr: String
        get() {
            val sdf = getDateFormat(DateFormat.YEAR_MONTH_DAY_HOURS)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            return sdf.format(System.currentTimeMillis())
        }

    /**
     * <pre>
     * [Convert] current date to dateString ( [DateFormat.YEAR_MONTH_DATE] )
    </pre> *
     *
     * @param d
     * @return
     */
    fun getCurrentYearMonthDateDotStr(d: Date?): String {
        return formatToString(DateFormat.YEAR_MONTH_DATE, d)
    }

    /**
     * <pre>
     * [Convert] current date to dateString ( [DateFormat.YEAR_MONTH_DATE] )
    </pre> *
     *
     * @param d
     * @return
     */
    @Throws(ParseException::class)
    fun parseCurrentYearMonthDateDotStr(s: String): Date {
        return parseToDate(DateFormat.YEAR_MONTH_DATE, s)
    }

    /**
     * <pre>
     * [Convert] current date to dateString ( [DateFormat.MONTH_DATE] )
    </pre> *
     *
     * @param d
     * @return
     */
    fun getCurrentMonthDateDotStr(d: Date?): String {
        return formatToString(DateFormat.MONTH_DATE, d)
    }

    /**
     * <pre>
     * [Calculate] calculate diff days.
    </pre> *
     *
     * @param dStart
     * @param dEnd
     * @return
     */
    fun calculateDiffDay(dStart: Date?, dEnd: Date?): Long {
        dStart?.run {
            dEnd?.run {
                return (dEnd.time - dStart.time) / MILLS_ONE_DAY
            }
        }
        return 0
    }

    fun checkSameYear(d1: Date?, d2: Date?): Boolean {
        val cal = calendar
        cal.time = d1
        val year1 = cal[Calendar.YEAR]
        cal.time = d2
        val year2 = cal[Calendar.YEAR]
        return year1 == year2
    }

    fun parseLongToDate(value: Long): Date {
        return Date(value)
    }

    companion object {
        @JvmStatic
        var instance: DateUtil? = null
            get() {
                if (field == null) {
                    field = DateUtil()
                }
                return field
            }
            private set
    }
}