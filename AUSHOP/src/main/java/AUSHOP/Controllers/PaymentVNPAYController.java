package AUSHOP.Controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import AUSHOP.Config.VNPAYConfig;
import AUSHOP.entity.DonHang;
import AUSHOP.repository.DonHangRepository;
import javassist.NotFoundException;

@Controller
//@RequestMapping("/khachhang")
public class PaymentVNPAYController {

	@Autowired
	private DonHangRepository donHangRepository;

	@GetMapping("payment-callback")
	public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response)
			throws IOException, NumberFormatException, NotFoundException {

		String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
		String maDH = queryParams.get("maDH");
		if (maDH != null && !maDH.equals("")) {
			if ("00".equals(vnp_ResponseCode)) {
				// Giao dịch thành công
				// Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
				DonHang donHang = donHangRepository.findById(Integer.parseInt(queryParams.get("maDH")))
						.orElseThrow(() -> new NotFoundException("Không tồn tại đơn hàng này!"));
				donHang.setTinhTrang(2);
				donHangRepository.save(donHang);
				response.sendRedirect("http://localhost:9090/customer/info");
			} else {
				// Giao dịch thất bại
				// Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
				response.sendRedirect("http://localhost:9090/customer/info");
			}
		}
	}

	@GetMapping("/payment-VNPAY")
	public String getPay(@RequestParam("maDH") Integer maDH, @RequestParam("tongTien") Float tongTien)
			throws UnsupportedEncodingException {

		String vnp_Version = "2.1.0";
		String vnp_Command = "pay";
		String orderType = "other";
		long amount = (long) (tongTien.floatValue() * 100);
		String bankCode = "NCB";

		String vnp_TxnRef = maDH.toString();
		String vnp_IpAddr = "127.0.0.1";

		String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;

		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");

		vnp_Params.put("vnp_BankCode", bankCode);
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
		vnp_Params.put("vnp_OrderType", orderType);

		vnp_Params.put("vnp_Locale", "vn");
		vnp_Params.put("vnp_ReturnUrl", VNPAYConfig.vnp_ReturnUrl + "?maDH=" + maDH);
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.secretKey, hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + queryUrl;

		return "redirect:" + paymentUrl;
	}
}
