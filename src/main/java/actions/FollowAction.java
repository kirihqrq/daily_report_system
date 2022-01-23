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

        //指定されたページ数の一覧画面に表示するフォローデータを取得
        int page = getPage();
        List<FollowView> follows = folService.getAllPerPage(page);

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
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException

    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //フォロー情報の空インスタンスに、フォローの日付＝今日の日付を設定する
        ReportView rv = new ReportView();
        rv.setReportDate(LocalDate.now());
        putRequestScope(AttributeConst.REPORT, rv); //日付のみ設定済みのフォローインスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_REP_NEW);

    }

    /**
     * 新規登録を行う
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
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            EmployeeView ev2 = empService.findOne(toNumber(getRequestParam(AttributeConst.EMPLOYEE)));


            //パラメータの値をもとにフォロー情報のインスタンスを作成する
            FollowView fv = new FollowView(
                    null,
                    ev, //ログインしている従業員を、フォロー作成者として登録する
                    ev2,//修正要＊＊＊
                    day);

            //フォロー情報登録
            List<String> errors = folService.create(fv);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.FOLLOW, fv);//入力されたフォロー情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //一覧画面表示
                forward(ForwardConst.FW_FOL_SHOW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());
              //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);

            }
        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException

    public void show() throws ServletException, IOException {

        //idを条件にフォローデータを取得する
        ReportView rv = folService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        if (rv == null) {
            //該当のフォローデータが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.REPORT, rv); //取得したフォローデータ

            //詳細画面を表示
            forward(ForwardConst.FW_REP_SHOW);
        }
    }
    */

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException

    public void edit() throws ServletException, IOException {

        //idを条件にフォローデータを取得する
        ReportView rv = folService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null || ev.getId() != rv.getEmployee().getId()) {
            //該当のフォローデータが存在しない、または
            //ログインしている従業員がフォローの作成者でない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.REPORT, rv); //取得したフォローデータ

            //編集画面を表示
            forward(ForwardConst.FW_REP_EDIT);
        }

    }
    */

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException

    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件にフォローデータを取得する
            ReportView rv = folService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            //入力されたフォロー内容を設定する
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            //フォローデータを更新する
            List<String> errors = folService.update(rv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv); //入力されたフォロー情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_REP_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);

            }
        }
    }
    */

    /**
     * ユーザページを表示する
     * @throws ServletException
     * @throws IOException
     * */

    public void showFollow() throws ServletException, IOException {


        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        EmployeeView employee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
        EmployeeView opponent = empService.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //を条件にフォローデータを取得する
        FollowView fv = folService.getByEmpAndOpp(employee, opponent);
        //FollowView fv = folService.findOne(toNumber(getRequestParam(AttributeConst.FOL_ID)));
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

            putRequestScope(AttributeConst.EMPLOYEE, ev); //取得したデータ
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