package idusw.jw202112049.pims.repository;

import idusw.jw202112049.pims.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAOImpl extends DAOImpl implements MemberDAO {
    // JDBC Objects 선언
    private Connection conn = null;
    private Statement stmt = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    // 생성자 - getConnection 메소드 호출, 연결 객체 반환
    public MemberDAOImpl() {
        conn = getConnection();
        if (conn == null) {
            throw new RuntimeException("데이터베이스 연결에 실패했습니다.");
        }
    }

    @Override
    public int create(Member member) { // 등록 : register, post
        int ret = 0;
        String sql = "insert into db_pims(email, pw, username, phone, address) values(?,?,?,?,?)";
        try {
            if (conn == null) {
                throw new SQLException("데이터베이스 연결이 없습니다.");
            }
            pstmt = conn.prepareStatement(sql); // prepared Statement 객체, 미리 컴파일
            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getPw());
            pstmt.setString(3, member.getUsername());
            pstmt.setString(4, member.getPhone());
            pstmt.setString(5, member.getAddress());
            ret = pstmt.executeUpdate(); // insert, update, delete 문 실행시 사용
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return ret; // 질의의 영향을 받은 row 의 수 반환, 0이면 오류, 1이상이면 정상
    }

    @Override
    public Member read(Member member) {
        Member retMember = null;
        String sql = "select * from db_pims where email=? and pw=?"; // 유일키로(unique key)로 조회
        try {
            if (conn == null) {
                throw new SQLException("데이터베이스 연결이 없습니다.");
            }
            pstmt = conn.prepareStatement(sql); // prepared Statement 객체, 미리 컴파일
            pstmt.setString(1, member.getEmail());
            pstmt.setString(2, member.getPw());
            rs = pstmt.executeQuery(); // select 문 실행히 executeQuery 사용
            if (rs.next()) { // rs.next()는 반환된 객체에 속한 요소가 있는지를 반환하고, 다름 요소로 접근
                retMember = setMemberRs(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return retMember;
    }

    @Override
    public List<Member> readList() { // read 유사
        ArrayList<Member> memberList = null;
        String sql = "select * from db_pims";
        try {
            if (conn == null) {
                throw new SQLException("데이터베이스 연결이 없습니다.");
            }
            stmt = conn.createStatement();  // Statement 객체 생성
            rs = stmt.executeQuery(sql);   // SQL 실행 결과 가져오기
            memberList = new ArrayList<>();
            while (rs.next()) {
                Member member = setMemberRs(rs);
                memberList.add(member); // element 집합체 List
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return memberList;
    }

    @Override
    public int update(Member member) {
        int ret = 0;
        String sql = "update db_pims set pw=?, username=?, phone=?, address=? where email=?";
        try {
            if (conn == null) {
                throw new SQLException("데이터베이스 연결이 없습니다.");
            }
            pstmt = conn.prepareStatement(sql); // prepared Statement 객체 생성
            pstmt.setString(1, member.getPw());
            pstmt.setString(2, member.getUsername());
            pstmt.setString(3, member.getPhone());
            pstmt.setString(4, member.getAddress());
            pstmt.setString(5, member.getEmail());
            ret = pstmt.executeUpdate(); // insert, update, delete 문 실행시 사용
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return ret; // 질의의 영향을 받은 row 의 수 반환
    }

    @Override
    public int delete(Member member) {
        int ret = 0;
        String sql = "delete from db_pims where email=?";
        try {
            if (conn == null) {
                throw new SQLException("데이터베이스 연결이 없습니다.");
            }
            pstmt = conn.prepareStatement(sql); // prepared Statement 객체 생성
            pstmt.setString(1, member.getEmail());
            ret = pstmt.executeUpdate(); // insert, update, delete 문 실행시 사용
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return ret; // 질의의 영향을 받은 row 의 수 반환
    }

    // ResultSet(DB 처리 결과 집합)을 Member 객체로 변환
    private Member setMemberRs(ResultSet rs) throws SQLException {
        Member retMember = new Member();
        retMember.setSeq(rs.getInt("seq"));
        retMember.setEmail(rs.getString("email"));
        retMember.setPw(rs.getString("pw"));
        retMember.setUsername(rs.getString("username"));
        retMember.setPhone(rs.getString("phone"));
        retMember.setAddress(rs.getString("address"));
        return retMember;
    }

    // 자원 해제 메소드 추가
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
