package com.zeffee.controller;

import com.zeffee.entity.Theme;
import com.zeffee.lib.Common;
import com.zeffee.lib.Wechat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.zeffee.dao.ThemeDAO;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by zeffee on 2017/6/2.
 */
@RestController
@Transactional
public class ThemeController {
    @Autowired
    private ThemeDAO themeDAO;

    @RequestMapping(value = "/addTheme", method = RequestMethod.POST)
    public Map<String, Object> addTheme(@RequestBody @Valid Theme theme, BindingResult result) {
        if (result.hasErrors()) {
            return Common.getResponseMap(500, result.getFieldError().getDefaultMessage());
        }

        if (theme.getEnd_time().before(theme.getStart_time())) {
            return Common.getResponseMap(500, "EndTime needs after StartTime!");
        }

        theme.setUid("qwertyui71234567890123456789");
        themeDAO.addTheme(theme);
        return Common.getResponseMap(200, theme.getTid());
    }

    @RequestMapping(value = "/deleteTheme/{tid}", method = RequestMethod.POST)
    public Map<String, Object> deleteTheme(@PathVariable(value = "tid") int tid) {
        int status = themeDAO.deleteTheme(tid) == 1 ? 200 : 500;

        return Common.getResponseMap(status);
    }

    @RequestMapping(value = "/getTheme", method = RequestMethod.GET)
    public Map<String, Object> getTheme() {
        String uid = "qwertyui71234567890123456789";
        List<Theme> votesList = themeDAO.getMyThemeList(uid);
        return Common.getResponseMap(200, votesList);
    }


    @RequestMapping(value = "/getOpenid", method = RequestMethod.GET)
    public Map<String, Object> verify(HttpSession session) {
        return Common.getResponseMap(200, session.getAttribute("openid"));
    }
}
