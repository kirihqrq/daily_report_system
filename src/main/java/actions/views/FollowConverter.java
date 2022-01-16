package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Follow;

/**
 * 従業員データのDTOモデル⇔Viewモデルの変換を行うクラス
 *
 */
public class FollowConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成する
     * @param fv FollowViewのインスタンス
     * @return Followのインスタンス
     */
    public static Follow toModel(FollowView fv) {

        return new Follow(
                fv.getId(),
                EmployeeConverter.toModel(fv.getEmployee()),
                EmployeeConverter.toModel(fv.getOpponent()),
                fv.getCreatedAt());
    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成する
     * @param f Followのインスタンス
     * @return FollowViewのインスタンス
     */
    public static FollowView toView(Follow f) {

        if(f == null) {
            return null;
        }

        return new FollowView(
                f.getId(),
                EmployeeConverter.toView(f.getEmployee()),
                EmployeeConverter.toView(f.getOpponent()),
                f.getCreatedAt());
    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデルのリスト
     * @return Viewモデルのリスト
     */
    public static List<FollowView> toViewList(List<Follow> list) {
        List<FollowView> evs = new ArrayList<>();

        for (Follow f : list) {
            evs.add(toView(f));
        }

        return evs;
    }

    /**
     * Viewモデルの全フィールドの内容をDTOモデルのフィールドにコピーする
     * @param e DTOモデル(コピー先)
     * @param ev Viewモデル(コピー元)
     */
    public static void copyViewToModel(Follow f, FollowView fv) {
        f.setId(fv.getId());
        f.setEmployee(EmployeeConverter.toModel(fv.getEmployee()));
        f.setEmployee(EmployeeConverter.toModel(fv.getOpponent()));
        f.setCreatedAt(fv.getCreatedAt());

    }

}