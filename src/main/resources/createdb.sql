--
-- PostgreSQL database dump
--

-- Started on 2009-09-23 23:12:55

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- TOC entry 322 (class 2612 OID 16386)
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: -
--

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 1498 (class 1259 OID 9957070)
-- Dependencies: 3
-- Name: account_statement; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE account_statement (
    id integer NOT NULL,
    market_id integer NOT NULL,
    selection_id integer NOT NULL,
    bet_id bigint NOT NULL,
    settled_date timestamp without time zone NOT NULL,
    amount numeric NOT NULL,
    selection_name character varying(150),
    market_name character varying(150),
    market_type character varying(150),
    full_market_name character varying(255),
    event_type_id integer,
    bet_category_type character varying(150),
    bet_type character varying(150),
    bet_size numeric,
    avg_price numeric,
    placed_date timestamp without time zone,
    start_date timestamp without time zone,
    commission boolean,
    state_machine_id character varying(100),
    state_name character varying(100)
);


SET default_with_oids = true;

--
-- TOC entry 1507 (class 1259 OID 10112557)
-- Dependencies: 3
-- Name: hist_runner_price; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE hist_runner_price (
    market_id integer NOT NULL,
    selection_id integer NOT NULL,
    record_time timestamp without time zone NOT NULL,
    in_play_delay smallint NOT NULL,
    last_price_matched numeric,
    price_to_back numeric,
    price_to_lay numeric
);


SET default_with_oids = false;

--
-- TOC entry 1505 (class 1259 OID 10112515)
-- Dependencies: 3
-- Name: market; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE market (
    market_id integer NOT NULL,
    market_name character varying(150) NOT NULL,
    market_type character varying(150) NOT NULL,
    market_status character varying(150) NOT NULL,
    market_time timestamp without time zone NOT NULL,
    suspend_time timestamp without time zone NOT NULL,
    sport_id integer NOT NULL,
    menu_path character varying(512) NOT NULL,
    event_hierarchy character varying(512) NOT NULL,
    country_code character varying(20) NOT NULL,
    number_of_runners integer NOT NULL,
    number_of_winners integer NOT NULL,
    bsb_market boolean NOT NULL,
    turning_in_play boolean NOT NULL
);


--
-- TOC entry 1494 (class 1259 OID 7929117)
-- Dependencies: 3
-- Name: market_details; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE market_details (
    market_id integer NOT NULL,
    market_time timestamp without time zone NOT NULL,
    market_suspend_time timestamp without time zone NOT NULL,
    menu_path character varying(1024),
    sport_name character varying(1024) NOT NULL,
    region_name character varying(1024) NOT NULL,
    num_of_winners integer NOT NULL,
    num_of_runners integer NOT NULL
);


--
-- TOC entry 1495 (class 1259 OID 7929123)
-- Dependencies: 3
-- Name: market_details_runner; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE market_details_runner (
    market_id integer NOT NULL,
    selection_id integer NOT NULL,
    selection_name character varying(50) NOT NULL
);


--
-- TOC entry 1506 (class 1259 OID 10112525)
-- Dependencies: 3
-- Name: runner; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE runner (
    market_id integer NOT NULL,
    selection_id integer NOT NULL,
    selection_name character varying(150) NOT NULL
);


--
-- TOC entry 1496 (class 1259 OID 7929126)
-- Dependencies: 3
-- Name: runner_state; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE runner_state (
    market_id integer NOT NULL,
    selection_id integer NOT NULL,
    state_name character varying(100) NOT NULL,
    state_machine_id character varying(100) NOT NULL,
    id integer NOT NULL
);


--
-- TOC entry 1502 (class 1259 OID 10062622)
-- Dependencies: 3
-- Name: runner_state_bet; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE runner_state_bet (
    bet_id bigint NOT NULL,
    runner_state_id integer NOT NULL,
    placed_date timestamp without time zone,
    price numeric NOT NULL
);


--
-- TOC entry 1503 (class 1259 OID 10063271)
-- Dependencies: 3
-- Name: runner_state_last_bet; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE runner_state_last_bet (
    bet_id bigint NOT NULL,
    placed_date timestamp without time zone NOT NULL,
    price numeric NOT NULL,
    size numeric NOT NULL,
    size_matched numeric NOT NULL,
    avg_price_matched numeric NOT NULL,
    size_cancelled numeric NOT NULL,
    matched_date timestamp without time zone NOT NULL,
    bet_type character varying(10) NOT NULL,
    runner_state_id integer NOT NULL
);


--
-- TOC entry 1504 (class 1259 OID 10063287)
-- Dependencies: 3
-- Name: runner_state_last_bet_runner; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE runner_state_last_bet_runner (
    total_amount_matched numeric NOT NULL,
    last_price_matched numeric NOT NULL,
    total_to_back numeric NOT NULL,
    total_to_lay numeric NOT NULL,
    price_to_back numeric NOT NULL,
    total_on_price_to_back numeric NOT NULL,
    price_to_lay numeric NOT NULL,
    total_on_price_to_lay numeric NOT NULL,
    bet_id bigint NOT NULL,
    near_sp numeric,
    far_sp numeric,
    actual_sp numeric,
    in_play boolean,
    price_slope numeric,
    price_slope_err numeric
);


--
-- TOC entry 1499 (class 1259 OID 10004223)
-- Dependencies: 1583 3
-- Name: statement_runner; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW statement_runner AS
    SELECT a.market_id, a.market_name, a.selection_id, a.selection_name, a.settled_date, sum(a.amount) AS amount FROM account_statement a GROUP BY a.settled_date, a.market_id, a.market_name, a.selection_id, a.selection_name ORDER BY a.settled_date DESC;


--
-- TOC entry 1500 (class 1259 OID 10004231)
-- Dependencies: 1584 3
-- Name: statement_runner_detailed; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW statement_runner_detailed AS
    SELECT a.market_id, a.market_name, a.selection_id, a.selection_name, a.settled_date, a.amount, md.sport_name, md.region_name, md.num_of_winners, md.num_of_runners, rs.state_name FROM statement_runner a, market_details md, runner_state rs WHERE (((a.market_id = md.market_id) AND (a.market_id = rs.market_id)) AND (a.selection_id = rs.selection_id)) ORDER BY a.settled_date DESC;


--
-- TOC entry 1497 (class 1259 OID 9957068)
-- Dependencies: 1498 3
-- Name: account_statement_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE account_statement_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1816 (class 0 OID 0)
-- Dependencies: 1497
-- Name: account_statement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE account_statement_id_seq OWNED BY account_statement.id;


--
-- TOC entry 1501 (class 1259 OID 10062598)
-- Dependencies: 1496 3
-- Name: runner_state_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE runner_state_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- TOC entry 1817 (class 0 OID 0)
-- Dependencies: 1501
-- Name: runner_state_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE runner_state_id_seq OWNED BY runner_state.id;


--
-- TOC entry 1777 (class 2604 OID 9957073)
-- Dependencies: 1497 1498 1498
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE account_statement ALTER COLUMN id SET DEFAULT nextval('account_statement_id_seq'::regclass);


--
-- TOC entry 1776 (class 2604 OID 10062600)
-- Dependencies: 1501 1496
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE runner_state ALTER COLUMN id SET DEFAULT nextval('runner_state_id_seq'::regclass);


--
-- TOC entry 1791 (class 2606 OID 9987360)
-- Dependencies: 1498 1498
-- Name: account_statement_pk; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY account_statement
    ADD CONSTRAINT account_statement_pk PRIMARY KEY (id);


--
-- TOC entry 1780 (class 2606 OID 7929137)
-- Dependencies: 1494 1494
-- Name: market_details_pk; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY market_details
    ADD CONSTRAINT market_details_pk PRIMARY KEY (market_id);


--
-- TOC entry 1804 (class 2606 OID 10112522)
-- Dependencies: 1505 1505
-- Name: market_pk_market_id; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY market
    ADD CONSTRAINT market_pk_market_id PRIMARY KEY (market_id);


--
-- TOC entry 1795 (class 2606 OID 10110685)
-- Dependencies: 1502 1502
-- Name: runner_state_bet_pk; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY runner_state_bet
    ADD CONSTRAINT runner_state_bet_pk PRIMARY KEY (bet_id);


--
-- TOC entry 1798 (class 2606 OID 10063278)
-- Dependencies: 1503 1503
-- Name: runner_state_last_bet_pk; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY runner_state_last_bet
    ADD CONSTRAINT runner_state_last_bet_pk PRIMARY KEY (bet_id);


--
-- TOC entry 1800 (class 2606 OID 10063280)
-- Dependencies: 1503 1503 1503
-- Name: runner_state_last_bet_runner_unique; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY runner_state_last_bet
    ADD CONSTRAINT runner_state_last_bet_runner_unique UNIQUE (runner_state_id, bet_type);


--
-- TOC entry 1784 (class 2606 OID 10062607)
-- Dependencies: 1496 1496
-- Name: runner_state_pk; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY runner_state
    ADD CONSTRAINT runner_state_pk PRIMARY KEY (id);


--
-- TOC entry 1788 (class 2606 OID 10062609)
-- Dependencies: 1496 1496 1496 1496
-- Name: runner_state_unique_key; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY runner_state
    ADD CONSTRAINT runner_state_unique_key UNIQUE (state_machine_id, market_id, selection_id);


--
-- TOC entry 1789 (class 1259 OID 10085607)
-- Dependencies: 1498
-- Name: account_statement_bet_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX account_statement_bet_id ON account_statement USING btree (bet_id);


--
-- TOC entry 1792 (class 1259 OID 9957077)
-- Dependencies: 1498
-- Name: account_statement_settled_date; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX account_statement_settled_date ON account_statement USING btree (settled_date);


--
-- TOC entry 1805 (class 1259 OID 10113769)
-- Dependencies: 1507 1507
-- Name: index_hist_runner_price; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_hist_runner_price ON hist_runner_price USING btree (market_id, record_time);


--
-- TOC entry 1778 (class 1259 OID 9933762)
-- Dependencies: 1494
-- Name: market_details_market_time; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX market_details_market_time ON market_details USING btree (market_time);


--
-- TOC entry 1781 (class 1259 OID 9987275)
-- Dependencies: 1494
-- Name: market_details_region_name; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX market_details_region_name ON market_details USING btree (region_name);


--
-- TOC entry 1782 (class 1259 OID 9987264)
-- Dependencies: 1494
-- Name: market_details_sport_name; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX market_details_sport_name ON market_details USING btree (sport_name);


--
-- TOC entry 1801 (class 1259 OID 10112523)
-- Dependencies: 1505
-- Name: market_index_market_time; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX market_index_market_time ON market USING btree (market_time);


--
-- TOC entry 1802 (class 1259 OID 10112524)
-- Dependencies: 1505
-- Name: market_index_sport_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX market_index_sport_id ON market USING btree (sport_id);


--
-- TOC entry 1793 (class 1259 OID 10085456)
-- Dependencies: 1502
-- Name: runner_state_bet_market_time; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX runner_state_bet_market_time ON runner_state_bet USING btree (placed_date);


--
-- TOC entry 1796 (class 1259 OID 10063286)
-- Dependencies: 1503
-- Name: runner_state_last_bet_bet_type; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX runner_state_last_bet_bet_type ON runner_state_last_bet USING btree (bet_type);


--
-- TOC entry 1785 (class 1259 OID 10075349)
-- Dependencies: 1496
-- Name: runner_state_state_machine_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX runner_state_state_machine_id ON runner_state USING btree (state_machine_id);


--
-- TOC entry 1786 (class 1259 OID 10111855)
-- Dependencies: 1496 1496 1496
-- Name: runner_state_state_machine_market_selection; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX runner_state_state_machine_market_selection ON runner_state USING btree (state_machine_id, market_id, selection_id);


--
-- TOC entry 1806 (class 2606 OID 7929144)
-- Dependencies: 1779 1494 1495
-- Name: market_details_runner_market_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY market_details_runner
    ADD CONSTRAINT market_details_runner_market_id_fkey FOREIGN KEY (market_id) REFERENCES market_details(market_id) ON DELETE CASCADE;


--
-- TOC entry 1810 (class 2606 OID 10112528)
-- Dependencies: 1505 1506 1803
-- Name: runner_fk_market_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY runner
    ADD CONSTRAINT runner_fk_market_id FOREIGN KEY (market_id) REFERENCES market(market_id) ON DELETE CASCADE;


--
-- TOC entry 1807 (class 2606 OID 10062625)
-- Dependencies: 1783 1502 1496
-- Name: runner_state; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY runner_state_bet
    ADD CONSTRAINT runner_state FOREIGN KEY (runner_state_id) REFERENCES runner_state(id) ON DELETE CASCADE;


--
-- TOC entry 1808 (class 2606 OID 10063281)
-- Dependencies: 1496 1783 1503
-- Name: runner_state_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY runner_state_last_bet
    ADD CONSTRAINT runner_state_fk FOREIGN KEY (runner_state_id) REFERENCES runner_state(id) ON DELETE CASCADE;


--
-- TOC entry 1809 (class 2606 OID 10063293)
-- Dependencies: 1504 1797 1503
-- Name: runner_state_last_bet_runner_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY runner_state_last_bet_runner
    ADD CONSTRAINT runner_state_last_bet_runner_fk FOREIGN KEY (bet_id) REFERENCES runner_state_last_bet(bet_id) ON DELETE CASCADE;


--
-- TOC entry 1815 (class 0 OID 0)
-- Dependencies: 3
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2009-09-23 23:12:55

--
-- PostgreSQL database dump complete
--
 