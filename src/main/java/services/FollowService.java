package services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.FollowConverter;
import actions.views.FollowView;
import constants.JpaConst;
import models.Follow;
import models.validators.FollowValidator;

/**
 * フォローテーブルの操作に関わる処理を行うクラス
 */
public class FollowService extends ServiceBase {

    /**
     * 指定した従業員が作成したフォローデータを、指定されたページ数の一覧画面に表示する分取得しFollowViewのリストで返却する
     * @param employee 従業員
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<FollowView> getMinePerPage(EmployeeView employee, int page) {

        List<Follow> follows = em.createNamedQuery(JpaConst.Q_FOL_GET_ALL_MINE, Follow.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return FollowConverter.toViewList(follows);
    }

    /**
     * 指定した従業員が作成したフォローデータの件数を取得し、返却する
     * @param employee
     * @return フォローデータの件数
     */
    public long countAllMine(EmployeeView employee) {

        long count = (long) em.createNamedQuery(JpaConst.Q_FOL_GET_BY_EMP_AND_OPP, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();

        return count;
    }

    /**
     * 指定されたページ数の一覧画面に表示するフォローデータを取得し、FollowViewのリストで返却する
     * @param page ページ数
     * @return 一覧画面に表示するデータのリスト
     */
    public List<FollowView> getAllPerPage(int page) {

        List<Follow> follows = em.createNamedQuery(JpaConst.Q_FOL_GET_ALL, Follow.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return FollowConverter.toViewList(follows);
    }

    /**
     * フォローテーブルのデータの件数を取得し、返却する
     * @return データの件数
     */
    public long countAll() {
        long follows_count = (long) em.createNamedQuery(JpaConst.Q_FOL_COUNT, Long.class)
                .getSingleResult();
        return follows_count;
    }

    /**
     * idを条件に取得したデータをFollowViewのインスタンスで返却する
     * @param id
     * @return 取得データのインスタンス
     */
    public FollowView findOne(int id) {
        return FollowConverter.toView(findOneInternal(id));
    }



    /**
     * idを条件にデータを1件取得する
     * @param id
     * @return 取得データのインスタンス
     */
    private Follow findOneInternal(int id) {
        return em.find(Follow.class, id);
    }

    /**
     * 画面から入力された日報の登録内容を元にデータを1件作成し、フォローテーブルに登録する
     * @param fv 登録内容
     * @return バリデーションで発生したエラーのリスト
     */
    public List<String> create(FollowView fv) {
        List<String> errors = FollowValidator.validate(fv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            fv.setCreatedAt(ldt);
            createInternal(fv);
        }

        //バリデーションで発生したエラーを返却（エラーがなければ0件の空リスト）
        return errors;
    }

    /**
     * フォローデータを1件登録する
     * @param fv フォローデータ
     */
    private void createInternal(FollowView fv) {

        em.getTransaction().begin();
        em.persist(FollowConverter.toModel(fv));
        em.getTransaction().commit();

    }

    /**
     * フォロー情報をEmployeeViewのインスタンスで返却する
     * @param f
     * @return 取得データのインスタンス 取得できない場合null
     */
    public FollowView getByEmpAndOpp(EmployeeView employee, EmployeeView opponent) {
        Follow f = null;
        try {
            //を条件に1件取得する
            f = em.createNamedQuery(JpaConst.Q_FOL_GET_BY_EMP_AND_OPP, Follow.class)
                    .setParameter(JpaConst.JPQL_PARM_EMP, EmployeeConverter.toModel(employee))
                    .setParameter(JpaConst.JPQL_PARM_OPP, EmployeeConverter.toModel(opponent))
                    .getSingleResult();
        } catch (NoResultException ex) {
        }

        return FollowConverter.toView(f);

    }

    /**
     * フォロー情報を削除する
     * @param fv
     */
    public void destroy(FollowView fv1) {
        em.getTransaction().begin();
        em.remove(findOneInternal(fv1.getId()));
        em.getTransaction().commit();
        em.close();
    }

}