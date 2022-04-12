package com.hhvvg.bilibilifork

import com.hhvvg.bilibilifork.network.parseCookies
import org.junit.Test

import org.junit.Assert.*

class NetUtilTest {
    private val emulatedCookieString = "buvid3=d123094yu9238t5y6qhroi2w345; i-wanna-go-back=-1; b_ut=5; _uuid=d2398y59234oh5t9wheorfhwgeor; buvid_fp=dao83yrasiorghiaegrifweh; buvid4=983y84rahrawirghq3ry9ehrfosaehrfiwegurt; fingerprint=di38y4592y9qwhriqeugr; buvid_fp_plain=undefined; bp_video_offset_447983453=564978946546446; bp_t_offset_447983453=2938145710423; CURRENT_FNVAL=4048; blackside_state=1; rpdid=|(um|Juk|Rlu0J'uYR~mk)YuJ; CURRENT_QUALITY=80; CURRENT_BLACKGAP=0; LIVE_BUVID=D9A93HR9AOFH; PVID=2; nostalgia_conf=-1; sid=12ieyqwi; SESSDATA=1209udaioshdiqw7s2214e; bili_jct=da89s9dy7q29dqghdf9sa; DedeUserID=432543562; DedeUserID__ckMd5=8dasyd93qrgh9q9; hit-dyn-v2=1; innersign=0; b_lsid=8D9ASD89A7DG8GQ3"

    @Test
    fun testParseCookies() {
        val cookies = parseCookies(emulatedCookieString)
        assertEquals(cookies["buvid3"], "d123094yu9238t5y6qhroi2w345")
        assertEquals(cookies["bili_jct"], "da89s9dy7q29dqghdf9sa")
    }
}
