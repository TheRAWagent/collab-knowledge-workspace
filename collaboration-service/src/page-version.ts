const versionByPage = new Map<string, number>()

export function getNextVersion(pageId: string) {
    const next = (versionByPage.get(pageId) ?? 0) + 1
    versionByPage.set(pageId, next)
    return next
}
