package idusw.jw202112049.pims.repository;

import idusw.jw202112049.pims.model.Member;

import java.util.List;

public interface MemberDAO {
    /* 데이터 처리의 기본 연산 : C.R.U.D ( Create, Read, Update, Delete) */
    int create(Member member);
    Member read(Member member); // read one object / record
    List<Member> readList();    // read objects / records
    int update(Member member);
    int delete(Member member);
}