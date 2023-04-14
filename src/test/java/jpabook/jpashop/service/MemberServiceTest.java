package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)          //스프링과 테스트 통합
@SpringBootTest                             //스프링 컨테이너 띄우고 테스트
@Transactional                              //테스트가 끝나면 트랜잭션을 강제로 롤백
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
//    @Rollback(false)                             //테스트가 끝나도 DB에 데이터를 롤백하지 않음.
    public void 회원가입() throws Exception{

        Member member = new Member();
        member.setName("kim");

        Long saveId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");


        memberService.join(member1);
        try{
            memberService.join(member2);
        }
        catch(IllegalStateException e){
            return ;
        }

        fail("예외가 발생해야 한다.");
    }
}