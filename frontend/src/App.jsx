import { useEffect, useState } from "react";
import "./App.css";

const API = "http://localhost:8080";

function App() {

    const [backendMessage, setBackendMessage] =
        useState("");

    const [contracts, setContracts] =
        useState([]);

    const [oldVersion, setOldVersion] =
        useState("employee_v1.txt");

    const [newVersion, setNewVersion] =
        useState("employee_v2.txt");

    const [compareResult, setCompareResult] =
        useState("");

    const [keyword, setKeyword] =
        useState("penalty");

    const [searchResult, setSearchResult] =
        useState("");

    const [complianceResult, setComplianceResult] =
        useState("");

    useEffect(() => {

        fetch(`${API}/api/test`)
            .then(response => response.json())
            .then(data => {
                setBackendMessage(data.message);
            })
            .catch(() => {
                setBackendMessage(
                    "Backend is not running."
                );
            });

        fetch(`${API}/api/contracts`)
            .then(response => response.json())
            .then(data => {
                setContracts(data);
            })
            .catch(() => {
                setContracts([]);
            });

    }, []);

    async function compareVersions() {

        const response =
            await fetch(
                `${API}/api/compare?old=${encodeURIComponent(oldVersion)}&new=${encodeURIComponent(newVersion)}`
            );

        const data =
            await response.json();

        setCompareResult(data.result || data.error);
    }

    async function searchClause() {

        const response =
            await fetch(
                `${API}/api/search?file=${encodeURIComponent(newVersion)}&keyword=${encodeURIComponent(keyword)}`
            );

        const data =
            await response.json();

        setSearchResult(data.result || data.error);
    }

    async function checkCompliance() {

        const response =
            await fetch(
                `${API}/api/compliance?file=${encodeURIComponent(newVersion)}`
            );

        const data =
            await response.json();

        setComplianceResult(
            data.result || data.error
        );
    }

    return (
        <div className="container">

            <header>
                <h1>
                    Contract & Compliance
                    <br />
                    Version-Intelligence Platform
                </h1>

                <p>
                    DSA-Based Contract Analysis System
                </p>
            </header>

            <section className="card">

                <h2>Backend Status</h2>

                <p className="status">
                    {backendMessage}
                </p>

            </section>

            <section className="card">

                <h2>Available Contracts</h2>

                {contracts.length === 0 ? (
                    <p>No contracts found.</p>
                ) : (
                    <ul>
                        {contracts.map(
                            (contract, index) => (
                                <li key={index}>
                                    {contract}
                                </li>
                            )
                        )}
                    </ul>
                )}

            </section>

            <section className="card">

                <h2>Compare Contract Versions</h2>

                <label>
                    Old Version
                </label>

                <select
                    value={oldVersion}
                    onChange={
                        event =>
                            setOldVersion(
                                event.target.value
                            )
                    }
                >
                    <option value="employee_v1.txt">
                        Employee Version 1
                    </option>

                    <option value="employee_v2.txt">
                        Employee Version 2
                    </option>
                </select>

                <label>
                    New Version
                </label>

                <select
                    value={newVersion}
                    onChange={
                        event =>
                            setNewVersion(
                                event.target.value
                            )
                    }
                >
                    <option value="employee_v1.txt">
                        Employee Version 1
                    </option>

                    <option value="employee_v2.txt">
                        Employee Version 2
                    </option>
                </select>

                <button
                    onClick={compareVersions}
                >
                    Compare Versions
                </button>

                {compareResult && (
                    <pre>
                        {compareResult}
                    </pre>
                )}

            </section>

            <section className="card">

                <h2>Search Contract Clause</h2>

                <input
                    type="text"
                    value={keyword}
                    onChange={
                        event =>
                            setKeyword(
                                event.target.value
                            )
                    }
                    placeholder="Enter keyword"
                />

                <button
                    onClick={searchClause}
                >
                    Search
                </button>

                {searchResult && (
                    <pre>
                        {searchResult}
                    </pre>
                )}

            </section>

            <section className="card">

                <h2>Compliance Analysis</h2>

                <button
                    onClick={checkCompliance}
                >
                    Check Compliance
                </button>

                {complianceResult && (
                    <pre>
                        {complianceResult}
                    </pre>
                )}

            </section>

            <footer>
                <p>
                    DSA Algorithms:
                    KMP | Rabin-Karp | Hashing |
                    LCS | Edit Distance
                </p>
            </footer>

        </div>
    );
}

export default App;