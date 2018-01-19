(ns status-im.test.protocol.web3.inbox
  (:require [status-im.protocol.web3.inbox :as inbox]
            [cljs.test :refer-macros [deftest is testing]]))

(deftest test-extract-enode-id
  (testing "Get enode id from enode uri"
    (is (= "08d8eb6177b187049f6c97ed3f6c74fbbefb94c7ad10bafcaf4b65ce89c314dcfee0a8bc4e7a5b824dfa08b45b360cc78f34f0aff981f8386caa07652d2e601b"
           (inbox/extract-enode-id "enode://08d8eb6177b187049f6c97ed3f6c74fbbefb94c7ad10bafcaf4b65ce89c314dcfee0a8bc4e7a5b824dfa08b45b360cc78f34f0aff981f8386caa07652d2e601b@163.172.177.138:40404"))))
  (testing "Get enode id from mailformed enode uri"
    (is (= ""
           (inbox/extract-enode-id "8d8eb6177b187049f6c97ed3f6c74fbbefb94c7ad10bafcaf4b65ce89c314dcfee0a8bc4e7a5b824dfa08b45b360cc78f34f0aff981f8386caa07652d2e601b@163.172.177.138:40404")))))

(def peers
  [{:id "08d8eb6177b187049f6c97ed3f6c74fbbefb94c7ad10bafcaf4b65ce89c314dcfee0a8bc4e7a5b824dfa08b45b360cc78f34f0aff981f8386caa07652d2e601b"
    :name "StatusIM/v0.9.9-unstable/linux-amd64/go1.9.2"}
   {:id "0f7c65277f916ff4379fe520b875082a56e587eb3ce1c1567d9ff94206bdb05ba167c52272f20f634cd1ebdec5d9dfeb393018bfde1595d8e64a717c8b46692f"
    :name "Geth/v1.7.2-stable/linux-amd64/go1.9.1"}])

(def enode "enode://08d8eb6177b187049f6c97ed3f6c74fbbefb94c7ad10bafcaf4b65ce89c314dcfee0a8bc4e7a5b824dfa08b45b360cc78f34f0aff981f8386caa07652d2e601b@163.172.177.138:40404")
(def enode2 "enode://12d8eb6177b187049f6c97ed3f6c74fbbefb94c7ad10bafcaf4b65ce89c314dcfee0a8bc4e7a5b824dfa08b45b360cc78f34f0aff981f8386caa07652d2e601b@163.172.177.138:40404")

(deftest test-registered-peer?
  (testing "Peer is registered"
    (is (inbox/registered-peer? peers enode))
    (is (not (inbox/registered-peer? peers enode2)))
    (is (not (inbox/registered-peer? [] enode)))))
