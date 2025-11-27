package kr.or.kosa.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;                         // JGit Git 객체
import org.eclipse.jgit.lib.Ref;                        // 브랜치 Ref 타입
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;  // Git repo builder
import org.springaicommunity.mcp.annotation.McpTool;
// MCP Tool 어노테이션
import org.springframework.stereotype.Service;


@Service
public class GitToolService {

    /**
     * MCP Tool : 로컬 Git 저장소의 브랜치 목록 조회
     *
     * args:
     *   repoPath : Git 저장소 루트 경로 (예: "C:/workspace/my-project")
     */
    @McpTool(
        name = "listBranches",
        description = "주어진 Git 저장소 경로의 로컬 브랜치 목록을 반환합니다."
    )
    public List<String> listBranches(Map<String, Object> args) throws Exception {
    	System.out.println("MCP 호출");
        String repoPath = (String) args.getOrDefault("repoPath", ".");
        // repoPath/.git 기준으로 저장소 찾기
        Path gitDir = Paths.get(repoPath, ".git");

        var builder = new FileRepositoryBuilder()
                .setGitDir(gitDir.toFile())
                .readEnvironment()
                .findGitDir();

        try (var repository = builder.build();
             var git = new Git(repository)) {

            List<Ref> branches = git.branchList().call();

            return branches.stream()
                    .map(ref -> ref.getName().replace("refs/heads/", ""))
                    .toList();
        }
    }
}

