package actions.views;

import java.util.ArrayList;
import java.util.List;

import constants.AttributeConst;
import constants.JpaConst;
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
                fv.getName(),
                fv.getOpponent(),
                fv.getOpponentCode(),
                fv.getCreatedAt(),
                fv.getDeleteFlag() == null
                        ? null
                        : fv.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
                                ? JpaConst.FOL_DEL_TRUE
                                : JpaConst.FOL_DEL_FALSE);
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
                f.getName(),
                f.getOpponent(),
                f.getOpponentCode(),
                f.getCreatedAt(),
                f.getDeleteFlag() == null
                        ? null
                        : f.getDeleteFlag() == JpaConst.FOL_DEL_TRUE
                                ? AttributeConst.DEL_FLAG_TRUE.getIntegerValue()
                                : AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
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
        f.setName(fv.getName());
        f.setOpponent(fv.getOpponent());
        f.setOpponentCode(fv.getOpponentCode());
        f.setCreatedAt(fv.getCreatedAt());
        f.setDeleteFlag(fv.getDeleteFlag());

    }

}