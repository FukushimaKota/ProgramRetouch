package ec;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.BuyDataBeans;
import beans.ItemDataBeans;
import beans.UserDataBeans;
import dao.BuyDAO;
import dao.BuyDetailDAO;
import dao.UserDAO;

/**
 * 購入履歴画面
 * @author d-yamaguchi
 *
 */
@WebServlet("/UserBuyHistoryDetail")
public class UserBuyHistoryDetail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
	// セッション開始
	try {
		// ログイン時に取得したユーザーIDをセッションから取得
		int userId = (int) session.getAttribute("userId");
		// 更新確認画面から戻ってきた場合Sessionから取得。それ以外はuserIdでユーザーを取得
		UserDataBeans udb = session.getAttribute("returnUDB") == null ? UserDAO.getUserDataBeansByUserId(userId) : (UserDataBeans) EcHelper.cutSessionAttribute(session, "returnUDB");

		// 入力された内容に誤りがあったとき等に表示するエラーメッセージを格納する
		String validationMessage = (String) EcHelper.cutSessionAttribute(session, "validationMessage");

		// URLからGETパラメータとしてIDを受け取る
		int buyId = Integer.parseInt(request.getParameter("buy_id"));

		BuyDataBeans  a = BuyDAO.getBuyDataBeansByBuyId(buyId);
		// リクエストスコープにセット
		request.setAttribute("a", a);

		//購入履歴詳細//購入履歴全件
		ArrayList<ItemDataBeans> aaa = BuyDetailDAO.getItemDataBeansListByBuyId(buyId);
		// リクエストスコープにセット
		request.setAttribute("aaa", aaa);



		request.setAttribute("validationMessage", validationMessage);
		request.setAttribute("udb", udb);

		request.getRequestDispatcher(EcHelper.USER_BUY_HISTORY_DETAIL_PAGE).forward(request, response);

	} catch (Exception e) {
		e.printStackTrace();
		session.setAttribute("errorMessage", e.toString());
		response.sendRedirect("Error");
	}
}
}