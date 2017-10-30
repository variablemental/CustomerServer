package com.emotibot.xychatlib.openapiresult.items;

import java.util.List;

/**
 * Created by ldy on 2017/3/6.
 */

public class XYlibNBAData {
    String answer;
    String url;
    List<MatchItem> matchlist;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MatchItem> getMatchlist() {
        return matchlist;
    }

    public void setMatchlist(List<MatchItem> matchlist) {
        this.matchlist = matchlist;
    }

    public class MatchItem {
        String teamimgurl1;
        String teamimgurl2;
        String teamname1;
        String teamname2;
        String teamscore1;
        String teamscore2;
        String matchurl;
        String describe;
        String state;

        public String getTeamimgurl1() {
            return teamimgurl1;
        }

        public void setTeamimgurl1(String teamimgurl1) {
            this.teamimgurl1 = teamimgurl1;
        }

        public String getTeamimgurl2() {
            return teamimgurl2;
        }

        public void setTeamimgurl2(String teamimgurl2) {
            this.teamimgurl2 = teamimgurl2;
        }

        public String getTeamname1() {
            return teamname1;
        }

        public void setTeamname1(String teamname1) {
            this.teamname1 = teamname1;
        }

        public String getTeamname2() {
            return teamname2;
        }

        public void setTeamname2(String teamname2) {
            this.teamname2 = teamname2;
        }

        public String getTeamscore1() {
            return teamscore1;
        }

        public void setTeamscore1(String teamscore1) {
            this.teamscore1 = teamscore1;
        }

        public String getTeamscore2() {
            return teamscore2;
        }

        public void setTeamscore2(String teamscore2) {
            this.teamscore2 = teamscore2;
        }

        public String getMatchurl() {
            return matchurl;
        }

        public void setMatchurl(String matchurl) {
            this.matchurl = matchurl;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
