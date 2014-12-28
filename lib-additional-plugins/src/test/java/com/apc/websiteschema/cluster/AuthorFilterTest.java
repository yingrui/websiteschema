/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apc.websiteschema.cluster;

import com.apc.websiteschema.cluster.fields.AuthorFilter;
import org.junit.Test;
import websiteschema.cluster.analyzer.Doc;

/**
 *
 * @author ray
 */
public class AuthorFilterTest {

    @Test
    public void should_delimit_incorrect_field() {
        AuthorFilter snf = new AuthorFilter();
        Doc doc = new Doc();
        doc.addField("AUTHOR", "黎成");
        doc.addField("AUTHOR", "2012年06月16日15:00");
        snf.filtering(doc);
        System.out.println(doc.getValues("AUTHOR"));
        assert (doc.getValue("AUTHOR").equals("黎成"));
    }

    @Test
    public void should_find_author_in_end_of_content_fangwenjun() {
        AuthorFilter snf = new AuthorFilter();
        Doc doc = new Doc();
        doc.addField("AUTHOR", "作者");
        doc.addField("AUTHOR", "2012年06月16日15:00");
        doc.addField("CONTENT", "本报专稿埃及总统选举第二轮投票于当地时间16日拉开帷幕。由于本轮选举将会产生前总统穆巴拉克下台后首位民选总统，因此受到广泛关注。但就在投票开始当日，埃及最高宪法法院做出判决，解散人民议会。这一判决给选举乃至埃及政局投下不定时炸弹。"
                + "　　第二轮投票将于16日至17日举行，初步统计结果有望于18日产生，但官方结果将于21日公布。对于决胜轮选票投给穆兄会的穆尔西还是前总理莎菲克，民众分歧很大。"
                + "　　虽然没有发生严重的安全事件，但就在当天，埃及最高宪法法院裁定议会选举所依据的法律部分条款违宪，新选出的人民议会无效并解散。根据埃及法律，最高宪法法院裁决不允许上诉。目前判决已进入执行阶段，议员未经许可不得进入议会大楼。"
                + "　　消息传出后，穆兄会成员福图赫指责最高宪法法院的裁决“等同于在搞政变”。穆兄会指责军方恋栈权力，呼吁就此举行全民公决，该党还指责军方企图在6月底交权前垄断所有权力。"
                + "　　由于现在正是投票期间，所以局势暂时未乱。不过，即使大选尘埃落定，也不会是埃及教俗两派角力的终点。不管最后谁获胜，另一派势力都难以接受。此外，穆兄会肯定不接受议会解散的裁决，暂时隐忍只为保住大选这一来之不易的胜利果实。因此，大选过后，埃及局势动荡难免。（方文军）"
                + "作者：方文军 (来源：新民晚报)");
        snf.filtering(doc);
        System.out.println(doc.getValues("AUTHOR"));
        assert (doc.getValue("AUTHOR").equals("方文军"));
    }

    @Test
    public void should_find_author_in_start_of_content_fanmeng() {
        AuthorFilter snf = new AuthorFilter();
        Doc doc = new Doc();
        doc.addField("AUTHOR", "作者");
        doc.addField("AUTHOR", "2012年06月16日15:00");
        doc.addField("CONTENT", "赵剑 记者 范萌"
                + "　　“激情欧洲杯参与竞猜，点击×××进入，天天都有奖”、“专卖店全面装修大清仓正在火爆进行，价格超低，欢迎抢购！”......每天清晨、或在半夜酣睡中，被卖楼、卖车、抽奖、促销等短信频频骚扰，这是大多数手机用户都遇到过的“折磨”，面对这些短信，也往往无奈地选择忍了。不堪垃圾短信骚扰的市民张女士，这次终于较真了。她将滥发商业短信给她的某品牌床上用品销售商告到了市工商局直属分局，请求给予惩处。"
                + "　　由于现在正是投票期间，所以局势暂时未乱。不过，即使大选尘埃落定，也不会是埃及教俗两派角力的终点。不管最后谁获胜，另一派势力都难以接受。此外，穆兄会肯定不接受议会解散的裁决，暂时隐忍只为保住大选这一来之不易的胜利果实。因此，大选过后，埃及局势动荡难免。（方文军）"
                + "作者：方文军 (来源：新民晚报)");
        snf.filtering(doc);
        System.out.println(doc.getValues("AUTHOR"));
        assert (doc.getValue("AUTHOR").equals("范萌"));
    }

    @Test
    public void should_find_multi_authors_in_start_of_content_fanmeng() {
        AuthorFilter snf = new AuthorFilter();
        Doc doc = new Doc();
        doc.addField("AUTHOR", "作者");
        doc.addField("AUTHOR", "2012年06月16日15:00");
        doc.addField("CONTENT", "大河网讯 (记者尚国傲 王涛) 位于郑州城北的琥珀名城小区，从来没有这么出名过，家住这里的中国首位女航天员的成功飞天，百余位记者的到来，让这个建成不久的小区，突然变得惊艳无比。");
        snf.filtering(doc);
        System.out.println(doc.getValues("AUTHOR"));
        assert (doc.getValue("AUTHOR").equals("尚国傲 王涛"));
    }

}
