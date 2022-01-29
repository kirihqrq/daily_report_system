package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.FollowView;

/**
 * フォローインスタンスに設定されている値のバリデーションを行うクラス
 */
public class FollowValidator {

    /**
     * フォローインスタンスの各項目についてバリデーションを行う
     * @param fv フォローインスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(FollowView fv) {
        List<String> errors = new ArrayList<String>();
        return errors;
    }
}