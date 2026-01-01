export class PageVersion {

  private static versionByPage = new Map<string, number>()

  static getNextVersion(pageId: string) {
    const next = (PageVersion.versionByPage.get(pageId) ?? 0) + 1
    PageVersion.versionByPage.set(pageId, next)
    return next;
  }

  static clearVersion(pageId: string) {
    PageVersion.versionByPage.delete(pageId);
  }

  static setVersion(pageId: string, version: number) {
    if (PageVersion.versionByPage.has(pageId)) {
      return;
    }
    PageVersion.versionByPage.set(pageId, version);
  }

}
