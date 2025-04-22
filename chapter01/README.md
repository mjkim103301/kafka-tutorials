# 1장. 카프카 개요
# 카프카 도입 배경

- 여러 네트워크를 이용하는 환경에서 모든 데이터 변경에 대한 올바른 전달 문제
- 동일한 데이터를 동시에 수정하면서 정확하게 순서 보장해야하는 문제
- 순서 보장해서 데이터를 아웃바운드로 전송해야 하는 문제
- 많은 클라이언트의 요구사항을 효율적으로 처리해야 하는 문제
- 빠른 전송을 위한 클라이언트를 지원해야 하는 문제
- 대용량 배치 전송을 위한 클라이언트를 지원해야 하는 문제

# 카프카 용어

### 카프카 스트림즈

- 끊임없이 전달되는 데이터셋 (Key-Value)
- 스트림 프로세서를 연결시켜 주는 연결선
    - 입력 스트림에서 데이터를 받아서 변환한 다음 연결된 프로세서에 보낸다.

![Image](https://github.com/user-attachments/assets/6b6cc795-c4f4-41ce-a5c6-c5988fbfb545)


### 카프카 맵리듀스

- Map + Reduce
- Map: 입력 파일을 한 줄씩 읽어서 데이터를 변형한다.
- Reduce: 맵의 결과 데이터를 집계한다.

![Image](https://github.com/user-attachments/assets/f89daa75-b281-40e9-a4c8-4161d3866fb3)

### 수평확장 용이 - 펍 섭 모델 지원, 강력한 파티셔닝

- 각각의 파티션마다 독립적인 데이터 스트림 형성할 수 있다. → 데이터를 병렬로 처리

![Image](https://github.com/user-attachments/assets/a2c9e617-a290-4745-9822-f211096ab7e4)
- 파티션 내부에서 각 메시지는 순서를 보장할 수 있는 오프셋을 가지고 있다.


    | Offset | Message |
    | --- | --- |
    | 0 | Message A |
    | 1 | Message B |
    | 2 | Message C |
    | 3 | Message D |
    | 4 | Message E |
- 파티션을 여러 노드에 분산시켜 저장할 수 있고, 다양한 컨슈머가 서로 다른 파티션에서 데이터를 동시에 읽을 수 있다.

![Image](https://github.com/user-attachments/assets/d01bcef1-f079-4a78-a801-741e0e7e2507)

**이벤트 버스는 fsync()를 하는 동안 블로킹을 하는 반면, 카프카는 OS에 의존해 백그라운드로 fsync()를 처리하고 제로카피를 사용**했습니다.

### fsync()

- 파일 변경사항을 디스크에 저장한다.

### 제로카피(zero copy)

- read(), send() 두 번의 시스템 콜로 인한 컨텍스트 스위칭, 데이터 복사에 따른 성능저하를 막을 수 있다.
- zero copy 의 transferTo() 를 통해 하나의 시스템 콜로 컨텍스트 스위칭 없이 데이터를 읽을 수 있다.

ex) nfs file byte copy → nio java copy 내부적으로 zero copy (40초 → 0.04초)

- file inputstream, outputstream → Paths.get, Files.write (아닐지도 모름 검증 필요)

### 아웃바운드

- 데이터가 서버 외부로 나가는 경우를 말한다.

### 고가용성

- 내장된 장애 조치 매커니즘을 사용하여 장애 없이 지속적으로 작동할 수 는 시스템 기능이다.

### 카프카 리플리케이션

- 메시지들을 여러 개로 복제해서 카프카 클러스터 내 브로커들에게 분산시키는 동작이다.

![Image](https://github.com/user-attachments/assets/2326d270-9a5b-4efb-a265-6fc6596ea8fe)

![Image](https://github.com/user-attachments/assets/4691072a-f36d-4d71-990e-be57381b7c6f)

- Leader: 데이터 읽기, 쓰기 가능하다.
- Follower: Leader에게 이슈가 있을경우 차기 Leader가 될 준비를 한다.. 지속적으로 새로운 메시지를 복제해서 Follower에 저장한다.
- 복구 방법이 더 문제

## 카프카 추가 내용

SNS + SQS → 카프카 전환 가능

- 은행 목록 동기화하는 기능에서 SNS+SQS 사용
- 업무 중지 은행은 목록에서 제외

비동기 서비스 → 카프카 전환 가능

---

참고자료

- zero copy: https://shawn-xu.medium.com/its-all-about-buffers-zero-copy-mmap-and-java-nio-50f2a1bfc05c
    - https://h-devnote.tistory.com/19
- java nio: https://developer.ibm.com/tutorials/j-nio/
- MSA 서비스 관리: https://velog.io/@beryl/Istio-%EA%B0%9C%EB%85%90
    - https://istio.io/latest/docs/ops/integrations/kiali/
- kong gateway: https://nginxstore.com/blog/kong/kong-gateway-%EB%9E%80/
- event bus: https://velog.io/@silmxmail/Event-Bus
- spring kafka: https://docs.spring.io/spring-kafka/reference/kafka/sending-messages.html
- kafka quick start: https://github.com/schooldevops/kafka-tutorials-with-kido/blob/main/01.kafka_install.md
    - apach kafka: https://kafka.apache.org/quickstart