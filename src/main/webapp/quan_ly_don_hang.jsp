<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.* , Model.*"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý đơn hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/Header.jsp"/>

<div class="container mt-4">
    <h3 class="text-center mb-4">Quản lý đơn hàng</h3>

<%
    String err = (String) request.getAttribute("error");
    if (err != null) {
%>
    <div class="alert alert-danger"><%= err %></div>
<%
    } else {
        List<Model_DonHang> orders = (List<Model_DonHang>) request.getAttribute("orders");
        Map<Integer, List<Model_ChiTietDonHang>> map =
              (Map<Integer, List<Model_ChiTietDonHang>>) request.getAttribute("detailsMap");
        if (orders == null || orders.isEmpty()) {
%>
    <div class="alert alert-info">Chưa có đơn hàng.</div>
<%
        } else {
%>

    <div class="accordion" id="orderAcc">
<%
      int idx = 0;
      for (Model_DonHang dh : orders) {
%>
      <div class="accordion-item">
        <h2 class="accordion-header" id="h<%=idx%>">
          <button class="accordion-button collapsed" type="button"
                  data-bs-toggle="collapse" data-bs-target="#c<%=idx%>">
            ĐH #<%= dh.getMaDonHang() %> – <%= dh.getHoTenNguoiDung() %>
            &nbsp;| <%= dh.getTrangThai() %> | 
            <%= dh.getNgayDat() %>
          </button>
        </h2>
        <div id="c<%=idx%>" class="accordion-collapse collapse" data-bs-parent="#orderAcc">
          <div class="accordion-body">
            <p><strong>Khách:</strong> <%= dh.getHoTenNguoiDung() %> –
               <%= dh.getSoDienThoaiNguoiDung() %></p>
            <p><strong>Địa chỉ giao:</strong> <%= dh.getDiaChiGiao() %></p>
            <p><strong>Ghi chú:</strong> <%= dh.getGhiChu()==null?"":dh.getGhiChu() %></p>

            <table class="table table-sm table-bordered align-middle">
              <thead class="table-light">
                <tr><th>Sản phẩm</th><th>Số lượng</th><th>Đơn giá</th><th>Thành tiền</th></tr>
              </thead>
              <tbody>
<%
                double thanhTien = 0;
                for (Model_ChiTietDonHang ct : map.get(dh.getMaDonHang())) {
                    double tt = ct.getDonGia() * ct.getSoLuong();
                    thanhTien += tt;
%>
                <tr>
                   <td><%= ct.getTenSanPham() %></td>
                   <td><%= ct.getSoLuong() %></td>
                   <td><%= String.format("%,.0f", ct.getDonGia()) %></td>
                   <td><%= String.format("%,.0f", tt) %></td>
                </tr>
<%
                }
%>
              </tbody>
              <tfoot>
                <tr><th colspan="3" class="text-end">Tổng:</th>
                    <th><%= String.format("%,.0f", thanhTien) %></th></tr>
              </tfoot>
            </table>
          </div>
        </div>
      </div>
<%
        idx++;
      } // end for
%>
    </div> <!-- accordion -->
<%
        }
    }
%>

    <a href="<%= request.getContextPath() %>/danh_sach_san_pham.jsp" class="btn btn-secondary mt-4">
        &laquo; Về trang Admin
    </a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
