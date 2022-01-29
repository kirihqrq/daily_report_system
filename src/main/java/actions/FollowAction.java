package actions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.FollowView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.EmployeeService;
import services.FollowService;

/**
 * フォローに関する処理を行うActionクラス
 *
 */
public class FollowAction extends ActionBase {

    private FollowService folService;
    private EmployeeService empService;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        folService = new FollowService();
        empService = new EmployeeService();

        //メソッドを実行
        invoke();
        folService.close();
        empService.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //ログイン中の従業員のフォローデータを、指定されたページ数の一覧画面に表示する分取得する
        int page = getPage();
        List<FollowView> follows = folService.getMinePerPage(loginEmployee, page);

        //全フォローデータの件数を取得
        long followsCount = folService.countAll();

        putRequestScope(AttributeConst.FOLLOWS, follows); //取得したデータ
        putRequestScope(AttributeConst.FOL_COUNT, followsCount); //全てのデータの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_FOL_INDEX);
    }

    /**
     * フォローを行う
     * @throws ServletException
     * @throws IOException
     */

    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //フォローの日付が入力されていなければ、今日の日付を設定
            LocalDateTime day = null;
            if (getRequestParam(AttributeConst.FOL_DATE) == null
                    || getRequestParam(AttributeConst.FOL_DATE).equals("")) {
                day = LocalDateTime.now();
            } else {
                day = LocalDateTime.parse(getRequestParam(AttributeConst.REP_DATE));
            }

            //セッションからログイン中の従業員情報を取得
            EmployeeView employee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            EmployeeView opponent = empService.findOne(toNumber(getRequestParam(AttributeConst.EMPLOYEE)));

            FollowView fv1 = folService.getByEmpAndOpp(employee, opponent);

            //パラメータの値をもとにフォロー情報のインスタンスを作成する
            FollowView fv = new FollowView(
                    null,
                    employee, //ログインしている従業員
                    opponent,//フォローする従業員
                    day);

            if(fv1 == null) {

                //フォロー情報登録
                List<String> errors = folService.create(fv);

                if (errors.size() > 0) {
                    //登録中にエラーがあった場合
                    putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                    putRequestScope(AttributeConst.FOLLOW, fv);//入力されたフォロー情報
                    putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                    //一覧画面表示
                    forward(ForwardConst.FW_FOL_INDEX);
                } else {
                    //登録中にエラーがなかった場合
                    //セッションに登録完了のフラッシュメッセージを設定
                    putSessionScope(AttributeConst.FLUSH, MessageConst.I_FOLLOWED.getMessage());
                    //一覧画面にリダイレクト
                    redirect(ForwardConst.ACT_FOL, ForwardConst.CMD_INDEX);

                }
            }else {
                folService.destroy(fv1);
                // セッションスコープ上の不要になったデータを削除
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UNFOLLOWED.getMessage());
                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_FOL, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * ユーザページを表示する
     * @throws ServletException
     * @throws IOException
     * */

    public void showFollow() throws ServletException, IOException {


        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        EmployeeView employee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        EmployeeView opponent = empService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //フォローデータを取得する
        FollowView fv = folService.getByEmpAndOpp(employee, opponent);

        if(fv == null) {
            request.setAttribute("followOn", "フォロー");
        } else {
            request.setAttribute("followOn", "フォロー中");
        }
        //idを条件に従業員データを取得する
        EmployeeView ev = empService.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));
        if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

            //データが取得できなかった、または論理削除されている場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        } else {

            putRequestScope(AttributeConst.EMPLOYEE, ev);
            putRequestScope(AttributeConst.FOLLOW, fv);

            String str1 = employee.getCode();
            String str2 = opponent.getCode();

            //ユーザーページを表示
            if(str1.equals(str2)) {
                forward(ForwardConst.FW_FOL_SHOW2);
            }else {
            forward(ForwardConst.FW_FOL_SHOW);
            }
        }
    }
}